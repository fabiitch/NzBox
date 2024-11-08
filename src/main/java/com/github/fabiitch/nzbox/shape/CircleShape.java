package com.github.fabiitch.nzbox.shape;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.github.fabiitch.nz.gdx.render.shape.NzShapeRenderer;
import com.github.fabiitch.nz.java.math.shapes.utils.CircleUtils;
import com.github.fabiitch.nzbox.contact.compute.ContactResolver;
import com.github.fabiitch.nzbox.contact.compute.ShapeContact;

public class CircleShape extends BodyShape<Circle> {

    public CircleShape(Circle shape) {
        super(shape);
    }

    public CircleShape(float radius) {
        this(new Circle(0, 0, radius));
    }

    @Override
    public Vector2 getPosition(Vector2 res) {
        return res.set(shape.x, shape.y);
    }

    @Override
    public void setPosition(float x, float y) {
        shape.setPosition(x, y);
    }

    @Override
    public void setRotation(float rotation) {

    }

    @Override
    public void rotate(float amount) {

    }

    @Override
    public void scale(float scale) {
        if (scale < 0)
            shape.radius /= scale;
        shape.radius *= scale;
    }

    @Override
    public void debugDraw(NzShapeRenderer shapeRenderer) {
        shapeRenderer.circle(shape);
    }

    @Override
    public ShapeContact getContactVisitor(ContactResolver resolver) {
        return resolver.get(this);
    }

    @Override
    public boolean testContact(ShapeContact contactVisitor) {
        return contactVisitor.testContact(shape);
    }

    @Override
    public Vector2 replace(ShapeContact visitor) {
        return visitor.replace(shape);
    }

    @Override
    public void computeBoundingRect() {
        CircleUtils.getRectBounds(this.shape, this.boundingRect);
    }

    @Override
    public boolean isValid() {
        return shape.radius > 0;
    }
}
