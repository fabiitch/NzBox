package com.github.fabiitch.nzbox;

import com.badlogic.gdx.utils.Array;
import com.github.fabiitch.nzbox.bodies.BodyType;
import com.github.fabiitch.nzbox.contact.ContactFixture;
import com.github.fabiitch.nzbox.contact.ContactListener;
import com.github.fabiitch.nzbox.contact.ContactListenerLogger;
import com.github.fabiitch.nzbox.contact.ContactUtils;
import com.github.fabiitch.nzbox.data.Body;
import com.github.fabiitch.nzbox.data.BoxData;
import com.github.fabiitch.nzbox.data.Fixture;
import com.github.fabiitch.nzbox.utils.BoxPools;
import lombok.Getter;

public class BoxWorld {

    @Getter
    private final BoxData data;

    private ContactListener contactListener = new ContactListenerLogger();

    private BoxPools pools;

    private float stepTime = 1 / 200F;
    private float accumulator = 0f;

    @Getter
    private boolean simulationRunning = false;

    public BoxWorld() {
        pools = new BoxPools();
        this.data = new BoxData(this, this.pools);
    }

    public void step(float dt) {
        simulationRunning = true;
        accumulator += dt;
        while (accumulator >= stepTime) {
            iteration();
            accumulator -= stepTime;
        }
        simulationRunning = false;
    }

    public void iteration() {
        Array<Body> bodies = data.getBodies();

        for (int i = 0, n = bodies.size; i < n; i++) {
            Body body = bodies.get(i);
            if (!body.isActive() || body.getBodyType() == BodyType.Static)
                continue;
            moveBody(body);
        }
        simulationRunning = false;
    }

    private void moveBody(Body body) {
        boolean move = body.move(stepTime);
        if (move || body.isDirty()) {
            checkBodyCollisions(body);
            body.setDirty(false);
        }
    }

    protected void checkBodyCollisions(Body bodyA) {
        for (int i = 0, n = bodyA.getFixtures().size; i < n; i++) {
            Fixture fixtureA = bodyA.getFixtures().get(i);
            if (!fixtureA.isActive())
                continue;
            checkFixtureCollision(bodyA, fixtureA);
        }
    }

    protected void checkFixtureCollision(Body bodyA, Fixture fixtureA) {
        for (Body bodyB : data.getBodies()) {
            if (bodyA != bodyB && bodyB.isActive() && ContactUtils.shouldTest(bodyA, bodyB)) {
                for (Fixture fixtureB : bodyB.getFixtures()) {
                    if (fixtureB.isActive() && ContactUtils.shouldTest(fixtureA, fixtureB)) {
                        fixtureCollision(fixtureA, fixtureB);
                    }
                }
            }
        }
    }

    private void fixtureCollision(Fixture fixtureA, Fixture fixtureB) {
        ContactFixture contactFixture = fixtureA.hasContact(fixtureB);
        if (contactFixture != null) {
            boolean isAlwaysContact = fixtureA.testContact(fixtureB);
            if (isAlwaysContact) {
                contactListener.continueContact(contactFixture);
                if (contactFixture.isReplace()) {
                    fixtureA.replace(fixtureB);
                }
            } else {
                contactListener.endContact(contactFixture);
                data.endContact(contactFixture);
            }

        } else {
            boolean isNewContact = fixtureA.testContact(fixtureB);
            if (isNewContact) {
                contactFixture = data.addContact(fixtureA, fixtureB);
                contactListener.beginContact(contactFixture);
                if (contactFixture.isReplace()) {
                    fixtureA.replace(fixtureB);
                }
            }
        }
    }

    public void addBody(Body body) {
        data.addBody(body);
    }
}
