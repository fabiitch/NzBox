package com.github.fabiitch.nzbox.pools;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.github.fabiitch.nzbox.contact.data.ContactBody;
import com.github.fabiitch.nzbox.contact.data.ContactFixture;
import com.github.fabiitch.nzbox.data.Body;
import com.github.fabiitch.nzbox.data.Fixture;

//TODO body, fixture, shape
public class BoxPools {

    private final Pool<Array<Fixture<?>>> fixtureArrayPool = new Pool<Array<Fixture<?>>>() {
        @Override
        protected Array<Fixture<?>> newObject() {
            return new Array<>();
        }
    };
    private final Pool<Array<Body>> bodyArrayPool = new Pool<Array<Body>>() {
        @Override
        protected Array<Body> newObject() {
            return new Array<>();
        }
    };

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


    public Array<Fixture<?>> getFixtureArray() {
        return fixtureArrayPool.obtain();
    }

    public void freeFixtureArray(Array<Fixture<?>> fixtureArray) {
        fixtureArray.clear();
        fixtureArrayPool.free(fixtureArray);
    }

    public Array<Body> getBodyArray() {
        return bodyArrayPool.obtain();
    }

    public void freeBodyArray(Array<Body> bodyArray) {
        bodyArray.clear();
        bodyArrayPool.free(bodyArray);
    }
}
