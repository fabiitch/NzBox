package com.github.fabiitch.nzbox.data;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.github.fabiitch.nz.java.data.quadtree.QuadTree;
import com.github.fabiitch.nzbox.contact.data.ContactFixture;
import com.github.fabiitch.nzbox.contact.compute.ContactResolver;
import com.github.fabiitch.nzbox.contact.compute.ShapeContact;
import com.github.fabiitch.nzbox.shape.BodyShape;
import lombok.Getter;
import lombok.Setter;

public class Fixture<S extends BodyShape<?>> {

    static ContactResolver contactResolver = new ContactResolver();

    @Getter
    int id;
    @Getter
    private Body body;
    @Getter
    private boolean active = true;
    @Getter
    private S bodyShape;
    @Getter
    private final Array<ContactFixture> contacts;
    @Getter
    private final Vector2 relativePos = new Vector2();
    @Getter
    private float relativeRotation;
    @Getter
    @Setter
    private boolean replaceBody = true;

    @Getter
    @Setter
    private Object userData;

    @Getter
    QuadTree quadTree;

    public Fixture(S bodyShape) {
        this.bodyShape = bodyShape;
        contacts = new Array<>();
    }

    public void setPosition(float x, float y, float rotation) {
        bodyShape.setPosition(relativePos.x + x, relativePos.y + y);
        bodyShape.setRotation(rotation + relativeRotation);
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public ContactFixture hasContact(Fixture fixtureB) {
        for (int i = 0, n = contacts.size; i < n; i++) {
            ContactFixture contactFixture = contacts.get(i);
            if (contactFixture.hasFixture(fixtureB)) {
                return contactFixture;
            }
        }
        return null;
    }

    public boolean testContact(Fixture fixtureB) {
        ShapeContact myVisitor = bodyShape.getContactVisitor(contactResolver);

        boolean b = fixtureB.getBodyShape().testContact(myVisitor);
        return b;
    }

    public void replace(Fixture fixtureB) {
        ShapeContact contactVisitor = bodyShape.getContactVisitor(contactResolver);

        Vector2 replace = fixtureB.bodyShape.replace(contactVisitor);

        body.setPosition(replace.x, replace.y, 0);
    }

    public Vector2 getPosition(Vector2 res) {
        return bodyShape.getPosition(res);
    }

    public void addContact(ContactFixture contactFixture) {
        contacts.add(contactFixture);
    }

    public void removeContact(ContactFixture contactFixture) {
        contacts.removeValue(contactFixture, true);
    }

    @Override
    public String toString() {
        if(userData != null){
            return "Body id=" + getId() + " - [" + userData.toString() + "]";
        }
        return "Body id=" + getId()  + " - [" + super.toString() + "]";
    }
}

