package com.github.fabiitch.nzbox;

import com.badlogic.gdx.utils.Array;
import com.github.fabiitch.nz.java.data.collections.utils.ArrayUtils;
import com.github.fabiitch.nzbox.bodies.BodyType;
import com.github.fabiitch.nzbox.contact.data.ContactFixture;
import com.github.fabiitch.nzbox.data.Body;
import com.github.fabiitch.nzbox.data.Fixture;

public class BoxWorldUtils {

    public static boolean shouldUpdateBody(Body body) {
        if (!body.isActive())
            return false;
        if (body.getBodyType() == BodyType.Static) return false;
        return true;
    }

    public static boolean shouldTestContact(Body bodyA, Body bodyB) {
        if (bodyA == bodyB)
            return false;
        if (!bodyA.isActive() || !bodyB.isActive())
            return false;

        return true;
    }

    public static void addFixtureContactArray(Fixture<?> fixture, Array<Fixture<?>> fixtureArray) {
        Array<ContactFixture> contacts = fixture.getContacts();
        for (int i = 0, n = contacts.size; i < n; i++) {
            ContactFixture contactFixture = contacts.get(i);
            Fixture<?> other = contactFixture.getOther(fixture);
            ArrayUtils.addIfNotPresent(fixtureArray, other);
        }
    }
}
