package com.github.fabiitch.nzbox.data;

import com.badlogic.gdx.utils.Array;
import com.github.fabiitch.nzbox.BoxWorld;
import com.github.fabiitch.nzbox.bodies.BodyType;
import com.github.fabiitch.nzbox.contact.data.ContactBody;
import com.github.fabiitch.nzbox.contact.data.ContactFixture;
import com.github.fabiitch.nzbox.contact.utils.ContactUtils;
import com.github.fabiitch.nzbox.data.quad.BoxQuadTree;
import com.github.fabiitch.nzbox.pools.BoxPools;
import com.github.fabiitch.nzbox.utils.IdGenerator;
import com.github.fabiitch.nzbox.utils.StaticIdGenerator;
import lombok.Getter;

@Getter
public class BoxData {

    private final BoxWorld world;
    private final IdGenerator bodyIdGenerator = new StaticIdGenerator();
    private final IdGenerator fixtureIdGenerator = new StaticIdGenerator();

    private final Array<Body> bodies = new Array<>();
    private final Array<Body> movingBodies = new Array<>();

    private final BoxPools boxPools;
    private final BoxQuadTree boxQuadTree;

    public BoxData(BoxWorld world, BoxPools boxPools) {
        this.world = world;
        this.boxPools = boxPools;
        boxQuadTree = new BoxQuadTree(boxPools);
    }

    public void addBody(Body body) {
        bodies.add(body);

        body.id = bodyIdGenerator.newId();
        body.world = world;

        for (Fixture fixture : body.getFixtures()) {
            addFixture(fixture);
        }
        if(body.isActive() && body.getBodyType()!= BodyType.Static)
            movingBodies.add(body);
    }

    public void removeBody(Body body) {
        bodyIdGenerator.free(body.id);
        bodies.removeValue(body, true);
        body.world = null;
        for (Fixture fixture : body.getFixtures()) {
            removeFixture(fixture);
        }
    }

    public void addFixture(Fixture fixture) {
        if (!fixture.getBodyShape().isValid()) {
            throw new IllegalArgumentException("Fixture shape is invalid");
        }
        fixture.id = fixtureIdGenerator.newId();
        boxQuadTree.addFixture(fixture);
    }

    public void removeFixture(Fixture fixture) {
        Array<ContactFixture> contacts = fixture.getContacts();
        for (ContactFixture contactFixture : contacts) {
            world.getContactListener().endContact(contactFixture);
        }
        boxQuadTree.remove(fixture);
    }

    public Body getBody(int id) {
        for (int i = 0, n = bodies.size; i < n; i++) {
            Body body = bodies.get(i);
            if (body.getId() == id)
                return body;
        }
        return null;
    }


    public ContactFixture addContact(Fixture fixtureA, Fixture fixtureB) {
        ContactFixture contactFixture = boxPools.getContactFixture(fixtureA, fixtureB);
        fixtureA.addContact(contactFixture);
        fixtureB.addContact(contactFixture);

        ContactBody contactBody = fixtureA.getBody().getContact(fixtureB.getBody());

        if (contactBody == null) {
            contactBody = boxPools.getContactBody(contactFixture);
            fixtureA.getBody().addContact(contactBody);
            fixtureB.getBody().addContact(contactBody);
        } else {
            contactBody.addContact(contactFixture);
        }

        contactFixture.setReplace(ContactUtils.shouldReplace(contactFixture));
        return contactFixture;
    }

    public void endContact(ContactFixture contactFixture) {
        Fixture fixtureA = contactFixture.getFixtureA();
        Fixture fixtureB = contactFixture.getFixtureB();

        Body bodyA = fixtureA.getBody();
        Body bodyB = fixtureB.getBody();

        fixtureA.removeContact(contactFixture);
        fixtureB.removeContact(contactFixture);

        ContactBody contactBody = bodyA.getContact(fixtureB.getBody());
        contactBody.removeContact(contactFixture);

        if (contactBody.getContactsFixture().isEmpty()) {
            bodyA.removeContact(contactBody);
            bodyB.removeContact(contactBody);
            boxPools.free(contactBody);
        }
        boxPools.free(contactFixture);
    }

}
