package com.github.fabiitch.nzbox;

import com.badlogic.gdx.utils.Array;
import com.github.fabiitch.nz.java.time.timers.TimeLocker;
import com.github.fabiitch.nzbox.contact.compute.ContactResolver;
import com.github.fabiitch.nzbox.contact.data.ContactFixture;
import com.github.fabiitch.nzbox.contact.listener.ContactListener;
import com.github.fabiitch.nzbox.contact.listener.ContactListenerLogger;
import com.github.fabiitch.nzbox.contact.utils.ContactUtils;
import com.github.fabiitch.nzbox.data.Body;
import com.github.fabiitch.nzbox.data.BoxData;
import com.github.fabiitch.nzbox.data.Fixture;
import com.github.fabiitch.nzbox.profiler.BoxProfiler;
import com.github.fabiitch.nzbox.pools.BoxPools;
import lombok.Getter;
import lombok.Setter;

public class BoxWorld {

    @Getter
    private final BoxData data;
    @Getter
    @Setter
    private ContactListener contactListener = new ContactListenerLogger();

    private final ContactResolver contactResolver = new ContactResolver();
    private final BoxPools pools;

    private float stepTime = 1f / 120f;
    private float accumulator = 0f;
    public boolean activeProfiler;

    @Getter
    private boolean simulationRunning = false;

    private BoxProfiler profiler;

    private TimeLocker timeLocker =  new TimeLocker(5f);
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

        if (timeLocker.isOpen(dt))
            data.getBoxQuadTree().updateQuad();
    }

    public void iteration() {
        Array<Body> bodies = data.getMovingBodies();

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
        if (activeProfiler) profiler.fixtureCheckCollision.inc();

        Array<Fixture<?>> fixturesClose = data.getBoxQuadTree().query(fixtureA.getBodyShape().getBoundingRect());

        for (int b = 0, n2 = fixturesClose.size; b < n2; b++) {
            Fixture fixtureB = fixturesClose.get(b);
            if (BoxWorldUtils.shouldTestContact(bodyA, fixtureB.getBody())) {   //TODO a voir vu qu'on a deja la liste
                fixtureTestCollision(fixtureA, fixtureB);
            }
        }
        pools.freeFixtureArray(fixturesClose);
    }

    private void fixtureTestCollision(Fixture<?> fixtureA, Fixture<?> fixtureB) {
        if (activeProfiler) profiler.fixtureCheckCollision.inc();

        ContactFixture hasContact = fixtureA.hasContact(fixtureB);
        if (hasContact != null) {
            if (!fastCheck(fixtureA, fixtureB)) {
                endContact(hasContact);
            } else {
                if (testContact(fixtureA, fixtureB)) {
                    fixtureA.replace(fixtureB, contactResolver);
                } else {
                    endContact(hasContact);
                }
            }
        } else {
            if (fastCheck(fixtureA, fixtureB)) {
                if (testContact(fixtureA, fixtureB)) {
                    beginContact(fixtureA, fixtureB);
                }
            }
        }
    }

    private boolean fastCheck(Fixture<?> fixtureA, Fixture<?> fixtureB) {
        if (activeProfiler) profiler.fastCheckContact.inc();
        return ContactUtils.fastCheck(fixtureA, fixtureB);
    }

    private boolean testContact(Fixture<?> fixtureA, Fixture<?> fixtureB) {
        if (activeProfiler) profiler.testContact.inc();
        return fixtureA.testContact(fixtureB, contactResolver);
    }

    private void beginContact(Fixture<?> fixtureA, Fixture<?> fixtureB) {
        if (activeProfiler) profiler.beginContact.inc();

        ContactFixture contactFixture = data.addContact(fixtureA, fixtureB);
        contactListener.beginContact(contactFixture);
        if (contactFixture.isReplace()) {
            fixtureA.replace(fixtureB, contactResolver);
        }
    }

    private void endContact(ContactFixture contactFixture) {
        if (activeProfiler) profiler.endContact.inc();
        if (contactFixture.isTriggerEnd())
            contactListener.endContact(contactFixture);
        data.endContact(contactFixture);
    }

    public void addBody(Body body) {
        data.addBody(body);
    }

    public void removeBody(Body body) {
        data.removeBody(body);
    }
}
