package com.github.fabiitch.nzbox.data;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.github.fabiitch.nzbox.BoxWorld;
import com.github.fabiitch.nzbox.bodies.BodyType;
import com.github.fabiitch.nzbox.contact.data.ContactBody;
import lombok.Getter;
import lombok.Setter;


public class Body {
    @Getter
    int id;
    @Getter
    BoxWorld world;

    @Getter
    private boolean active = true;
    @Getter
    private BodyType bodyType;

    @Getter
    private Array<Fixture> fixtures = new Array<>();
    @Getter
    public final Array<ContactBody> contacts = new Array<>();

    @Getter
    @Setter
    private boolean dirty;

    @Getter
    private final Vector2 position = new Vector2();
    @Getter
    private final Vector2 velocity = new Vector2();
    @Getter
    private float angularVelocity;//Degrees
    @Getter
    private float rotation = 0; //Degrees

    @Getter
    @Setter
    private Object userData;

    public Body(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    private final Vector2 tmp = new Vector2();

    public void addInWorld(int id, BoxWorld world) {
        this.id = id;
        this.world = world;

    }

    public boolean move(float dt) {
        if (velocity.isZero() && angularVelocity == 0)
            return false;

        position.add(tmp.set(velocity).scl(dt));
        rotation += angularVelocity * dt;

        updatePosition();
        return true;
    }

    private void updatePosition() {
        for (int i = 0, n = fixtures.size; i < n; i++) {
            Fixture fixture = fixtures.get(i);
            if (fixture.isActive()) {
                fixture.setPosition(position.x, position.y, rotation);
                fixture.getBodyShape().computeBoundingRect();
            }
        }
    }

    public void setPosition(Vector2 position, float rotation) {
        setPosition(position.x, position.y, rotation);
    }

    public void setPosition(float x, float y, float rotation) {
        this.position.set(x, y);
        this.rotation = rotation;
        updatePosition();
        dirty = true;
    }

    public void addFixture(Fixture fixture) {
        fixtures.add(fixture);
        fixture.setBody(this);
        fixture.setPosition(this.position.x, this.position.y, this.rotation);
        dirty = true;
    }

    public boolean removeFixture(Fixture fixture) {
        world.getData().removeFixture(fixture);
        return fixtures.removeValue(fixture, true);
    }

    public ContactBody getContact(Body bodyB) {
        for (ContactBody contact : contacts) {
            if (contact.has(bodyB))
                return contact;
        }
        return null;
    }


    public void setVelocity(float x, float y) {
        this.velocity.set(x, y);
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity.set(velocity);
    }

    void addContact(ContactBody contactBody) {
        contacts.add(contactBody);
    }

    void removeContact(ContactBody contactBody) {
        contacts.removeValue(contactBody, true);
    }
}
