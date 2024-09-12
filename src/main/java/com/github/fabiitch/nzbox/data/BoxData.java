package com.github.fabiitch.nzbox.data;

import com.badlogic.gdx.utils.Array;
import com.github.fabiitch.nz.java.data.quadtree.QuadTree;
import com.github.fabiitch.nzbox.BoxWorld;
import com.github.fabiitch.nzbox.contact.ContactBody;
import com.github.fabiitch.nzbox.contact.ContactFixture;
import com.github.fabiitch.nzbox.contact.ContactUtils;
import com.github.fabiitch.nzbox.utils.BoxPools;
import com.github.fabiitch.nzbox.utils.IdGenerator;
import com.github.fabiitch.nzbox.utils.StaticIdGenerator;
import lombok.Getter;

@Getter
public class BoxData {

    private final BoxWorld world;
    private IdGenerator bodyIdGenerator = new StaticIdGenerator();
    private IdGenerator fixtureIdGenerator = new StaticIdGenerator();

    private final Array<Body> bodies = new Array<>();
    private QuadTree<Fixture<?>> quadTree = new QuadTree<>();

    private final BoxPools boxPools;

    public BoxData(BoxWorld world, BoxPools boxPools) {
        this.world = world;
        this.boxPools = boxPools;
    }

    public void addBody(Body body) {
        bodies.add(body);

        body.id = bodyIdGenerator.newId();
        body.world = world;

        for (Fixture fixture : body.getFixtures()) {
            addFixture(fixture);
        }
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
        fixture.id = fixtureIdGenerator.newId();
        quadTree.add(fixture, fixture.getBodyShape().getBoundingRect());
    }

    public void removeFixture(Fixture fixture) {
        Array<ContactFixture> contacts = fixture.getContacts();
        for (ContactFixture contactFixture : contacts) {
            world.getContactListener().endContact(contactFixture);
        }
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
