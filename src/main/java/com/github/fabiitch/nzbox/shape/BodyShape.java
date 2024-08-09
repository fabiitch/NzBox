package com.github.fabiitch.nzbox.shape;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.github.fabiitch.nz.gdx.render.NzShapeRenderer;
import com.github.fabiitch.nzbox.contact.compute.ContactResolver;
import com.github.fabiitch.nzbox.contact.compute.ShapeContact;
import lombok.Getter;

@Getter
public abstract class BodyShape<S extends Shape2D> {

    protected S shape;
    protected final Rectangle boundingRect = new Rectangle();

    public BodyShape(S shape) {
        this.shape = shape;
    }

    public abstract Vector2 getPosition(Vector2 res);

    public final void setPosition(Vector2 position) {
        setPosition(position.x, position.y);
    }

    public abstract void setPosition(float x, float y);

    public abstract void setRotation(float rotation);

    public abstract void rotate(float amount);

    public abstract void scale(float scale);

    public abstract void debugDraw(NzShapeRenderer shapeRenderer);

    public abstract ShapeContact getContactVisitor(ContactResolver resolver);

    public abstract boolean testContact(ShapeContact contactVisitor);

    public abstract Vector2 replace(ShapeContact visitor);

    public abstract void computeBoundingRect();
}
