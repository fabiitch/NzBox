package com.github.fabiitch.nzbox.contact;


import com.github.fabiitch.nzbox.data.Body;
import com.github.fabiitch.nzbox.data.Fixture;

public class ContactUtils {


    public static boolean shouldTest(Body bodyA, Body bodyB) {
        return true;
    }

    public static boolean shouldTest(Fixture fixtureA, Fixture fixtureB) {
        return true;
    }

    public static boolean shouldReplace(ContactFixture contactFixture){
        return true;
    }
}
