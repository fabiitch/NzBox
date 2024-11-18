package com.github.fabiitch.nzbox.data;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.github.fabiitch.nz.java.data.quadtree.QuadTree;
import com.github.fabiitch.nzbox.contact.data.ContactFixture;
import com.github.fabiitch.nzbox.contact.compute.ContactResolver;
import com.github.fabiitch.nzbox.contact.compute.ShapeContact;
import com.github.fabiitch.nzbox.shape.BodyShape;
import lombok.Getter;
import lombok.Setter;


@Getter
public class Fixture<S extends BodyShape<?>> {

    static ContactResolver contactResolver = new ContactResolver();

    int id;
    @Setter
    private Body body;
    private boolean active = true;
    private S bodyShape;
    private final Array<ContactFixture> contacts= new Array<>();;
    private final Vector2 relativePos = new Vector2();
    @Setter
    private Object userData;

    public Fixture(S bodyShape) {
        this.bodyShape = bodyShape;
    }

    void setPosition(float x, float y, float rotation) {
        bodyShape.setPosition(relativePos.x + x, relativePos.y + y);
        bodyShape.setRotation(rotation);
        this.bodyShape.computeBoundingRect();
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

    void addContact(ContactFixture contactFixture) {
        contacts.add(contactFixture);
    }

    void removeContact(ContactFixture contactFixture) {
        contacts.removeValue(contactFixture, true);
    }

    @Override
    public String toString() {
        if (userData != null) {
            return "Body id=" + getId() + " - [" + userData.toString() + "]";
        }
        return "Body id=" + getId() + " - [" + super.toString() + "]";
    }

    public Rectangle getBoundingRectangle() {
        return bodyShape.getBoundingRect();
    }
}
