package com.github.fabiitch.nzbox.contact;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.github.fabiitch.nzbox.data.Body;
import lombok.Getter;
import lombok.Setter;

public class ContactBody implements Pool.Poolable {

    @Getter
    @Setter
    private Body bodyA, bodyB;

    @Getter
    private final Array<ContactFixture> contactsFixture = new Array<>();

    public boolean has(Body body) {
        return bodyA == body || bodyB == body;
    }

    public Body getOther(Body body) {
        if (bodyA == body) {
            return bodyB;
        }
        return body;
    }

    public boolean imBodyB(Body body) {
        return bodyB == body;
    }

    public void addContact(ContactFixture contactFixture) {
        contactsFixture.add(contactFixture);
    }

    public void removeContact(ContactFixture contactFixture) {
        contactsFixture.removeValue(contactFixture, true);
    }

    @Override
    public void reset() {
        bodyA = null;
        bodyB = null;
        contactsFixture.clear();
    }

    public String debug() {
        return "ContactBody=[BodyA=" + bodyA.getUserData() + "&& BodyB=" + bodyB.getUserData() + " ==> " + contactsFixture.size + " contacts]";
    }
}
