package com.github.fabiitch.nzbox.data;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.github.fabiitch.nz.java.data.quadtree.QuadTree;
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


public class BoxData {

    private final BoxWorld world;
    private final IdGenerator bodyIdGenerator = new StaticIdGenerator();
    private final IdGenerator fixtureIdGenerator = new StaticIdGenerator();
    @Getter
    private final Array<Body> bodies = new Array<>();
    @Getter
    private final Array<Body> movingBodies = new Array<>();
    @Getter
    private final BoxPools pools;
    private final BoxQuadTree boxQuadTree;
    @Getter
    private final QuadTree<Fixture<?>> quadTree;

    public BoxData(BoxWorld world, BoxPools pools) {
        this.world = world;
        this.pools = pools;
        this.quadTree = new QuadTree<>(new Rectangle(),20 ,10);
        this.boxQuadTree = new BoxQuadTree(quadTree);
    }

    public void addBody(Body body) {
        bodies.add(body);

        body.id = bodyIdGenerator.newId();
        body.world = world;

        for (Fixture fixture : body.getFixtures()) {
            addFixture(fixture);
        }
        if (body.isActive() && body.getBodyType() != BodyType.Static)
            movingBodies.add(body);
    }

    public void removeBody(Body body) {
        if (bodies.removeValue(body, true)) {
            if(body.getBodyType() != BodyType.Static){
                movingBodies.removeValue(body, true);
            }
            bodyIdGenerator.free(body.id);
            body.world = null;
            for (Fixture fixture : body.getFixtures()) {
                removeFixture(fixture);
            }
        } else {
            //TODO looger
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
            endContact(contactFixture);
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
        ContactFixture contactFixture = pools.getContactFixture(fixtureA, fixtureB);
        fixtureA.addContact(contactFixture);
        fixtureB.addContact(contactFixture);

        ContactBody contactBody = fixtureA.getBody().getContact(fixtureB.getBody());

        if (contactBody == null) {
            contactBody = pools.getContactBody(contactFixture);
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
            pools.free(contactBody);
        }
        pools.free(contactFixture);
    }

    public void updateBody(Body body) {
        if (body.getWorld() != null)
            for (int i = 0, n = body.getFixtures().size; i < n; i++)
                boxQuadTree.update(body.getFixtures().get(i));
    }

    public Array<Fixture<?>> query(Rectangle boundingRect) {
        Array<Fixture<?>> fixtureArray = pools.getFixtureArray();
        return quadTree.query(boundingRect, fixtureArray);
    }

    public Array<Fixture<?>> getFixtureClose(Fixture fixtureA, Array<Fixture<?>> results) {
        boxQuadTree.getFixtureClose(fixtureA, results);
        return results;
    }
}
