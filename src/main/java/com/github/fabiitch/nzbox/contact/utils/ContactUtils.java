package com.github.fabiitch.nzbox.contact.utils;


import com.github.fabiitch.nz.java.math.shapes.utils.RectangleUtils;
import com.github.fabiitch.nzbox.bodies.BodyType;
import com.github.fabiitch.nzbox.contact.data.ContactFixture;
import com.github.fabiitch.nzbox.data.Fixture;

public class ContactUtils {

    public static boolean shouldReplace(ContactFixture contactFixture) {
        BodyType bodyTypeA = contactFixture.getFixtureA().getBody().getBodyType();
        BodyType bodyTypeB = contactFixture.getFixtureB().getBody().getBodyType();

        if (bodyTypeA == BodyType.Phantom || bodyTypeB == BodyType.Phantom) {
            return false;
        }
        if (bodyTypeA == BodyType.Kinematic && bodyTypeB == BodyType.Kinematic) {
            return true;
        }
        return true;
    }


    public static boolean isKinematicOrPhantom(BodyType bodyType) {
        return bodyType == BodyType.Kinematic || bodyType == BodyType.Phantom;
    }

    public static boolean fastCheck(Fixture<?> fixtureA, Fixture<?> fixtureB) {
        return RectangleUtils.overlapsStick(fixtureA.getBoundingRectangle(), fixtureB.getBoundingRectangle());
    }
}
