package com.github.fabiitch.nzbox.utils;

import com.badlogic.gdx.utils.Pools;
import com.github.fabiitch.nzbox.contact.data.ContactBody;
import com.github.fabiitch.nzbox.contact.data.ContactFixture;
import com.github.fabiitch.nzbox.data.Fixture;

//TODO body, fixture, shape
public class BoxPools {
    public ContactFixture getContactFixture(Fixture fixtureA, Fixture fixtureB) {
        ContactFixture contactFixture = Pools.obtain(ContactFixture.class);
        contactFixture.setFixtureA(fixtureA);
        contactFixture.setFixtureB(fixtureB);
        return contactFixture;
    }

    public ContactBody getContactBody(ContactFixture contactFixture) {
        ContactBody contactBody = Pools.obtain(ContactBody.class);
        contactBody.addContact(contactFixture);
        contactBody.setBodyA(contactFixture.getFixtureA().getBody());
        contactBody.setBodyB(contactFixture.getFixtureB().getBody());
        return contactBody;
    }

    public void free(ContactFixture contactFixture) {
        Pools.free(contactFixture);
    }

    public void free(ContactBody contactBody) {
        Pools.free(contactBody);
    }
}
