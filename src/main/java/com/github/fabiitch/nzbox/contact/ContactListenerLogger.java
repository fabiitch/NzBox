package com.github.fabiitch.nzbox.contact;


import com.github.fabiitch.nz.gdx.log.StrFormat;
import com.github.fabiitch.nzbox.data.Body;
import com.github.fabiitch.nzbox.data.Fixture;

public class ContactListenerLogger implements ContactListener {
    @Override
    public void beginContact(ContactFixture contactFixture) {
        Fixture fixtureA = contactFixture.getFixtureA();
        Fixture fixtureB = contactFixture.getFixtureB();
        Body bodyA = fixtureA.getBody();
        Body bodyB = fixtureB.getBody();
        String format = StrFormat.format("[Begin contact] b:{}/f:{} | b:{}/f:{}",
                bodyA.getId(), fixtureA.getId(),
                bodyB.getId(), fixtureB.getId());

        System.out.println(format);
    }

    @Override
    public void endContact(ContactFixture contactFixture) {
        Fixture fixtureA = contactFixture.getFixtureA();
        Fixture fixtureB = contactFixture.getFixtureB();
        Body bodyA = fixtureA.getBody();
        Body bodyB = fixtureB.getBody();
        String format = StrFormat.format("[End contact] b:{}/f:{} | b:{}/f:{}",
                bodyA.getId(), fixtureA.getId(),
                bodyB.getId(), fixtureB.getId());

        System.out.println(format);
    }
}
