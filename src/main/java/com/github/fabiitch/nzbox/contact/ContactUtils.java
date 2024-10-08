package com.github.fabiitch.nzbox.contact;


import com.github.fabiitch.nzbox.bodies.BodyType;
import com.github.fabiitch.nzbox.data.Body;
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

}
