package com.github.fabiitch.nzbox;

import com.badlogic.gdx.utils.Array;
import com.github.fabiitch.nzbox.contact.data.ContactFixture;
import com.github.fabiitch.nzbox.contact.listener.ContactListener;
import com.github.fabiitch.nzbox.contact.listener.ContactListenerLogger;
import com.github.fabiitch.nzbox.data.Body;
import com.github.fabiitch.nzbox.data.BoxData;
import com.github.fabiitch.nzbox.data.Fixture;
import com.github.fabiitch.nzbox.profiler.BoxProfiler;
import com.github.fabiitch.nzbox.utils.BoxPools;
import lombok.Getter;
import lombok.Setter;

public class BoxWorld {

    @Getter
    private final BoxData data;
    @Getter
    @Setter
    private ContactListener contactListener = new ContactListenerLogger();

    private BoxPools pools;

    private float stepTime = 1 / 200F;
    private float accumulator = 0f;
    public boolean activeProfiler;

    @Getter
    private boolean simulationRunning = false;

    private BoxProfiler profiler;

    public BoxWorld() {
        pools = new BoxPools();
        this.data = new BoxData(this, this.pools);
    }

    public void step(float dt) {
        simulationRunning = true;

        if (activeProfiler) profiler.startStep(dt);

        accumulator += dt;
        while (accumulator >= stepTime && simulationRunning) {
            if (activeProfiler) profiler.startIteration();
            iteration();
            if (activeProfiler) profiler.endIteration();
            accumulator -= stepTime;
        }

        if (activeProfiler) profiler.endStep();
        simulationRunning = false;
    }

    public void iteration() {
        Array<Body> bodies = data.getBodies();

        for (int i = 0, n = bodies.size; i < n; i++) {
            Body body = bodies.get(i);
            if (!BoxWorldUtils.shouldUpdateBody(body))
                continue;
            moveBody(body);
        }
    }

    private void moveBody(Body body) {
        if (activeProfiler) profiler.bodyMove.inc();

        boolean move = body.move(stepTime);
        if (move || body.isDirty()) {
            checkBodyCollisions(body);
            body.setDirty(false);
        }
    }

    protected void checkBodyCollisions(Body bodyA) {
        if (activeProfiler) profiler.bodyCheckCollision.inc();

        for (int i = 0, n = bodyA.getFixtures().size; i < n; i++) {
            Fixture fixtureA = bodyA.getFixtures().get(i);
            if (!fixtureA.isActive())
                continue;
            checkFixtureCollision(bodyA, fixtureA);
        }
    }

    protected void checkFixtureCollision(Body bodyA, Fixture fixtureA) {
        for (Body bodyB : data.getBodies()) {
            if (bodyA != bodyB && bodyB.isActive()) {
                for (Fixture fixtureB : bodyB.getFixtures()) {
                    if (fixtureB.isActive()) {
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

    public void removeBody(Body body) {
        data.removeBody(body);
    }
}
