package com.github.fabiitch.nzbox.shape;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.github.fabiitch.nz.gdx.render.NzShapeRenderer;
import com.github.fabiitch.nz.java.math.shapes.utils.rectangle.RectangleUtils;
import com.github.fabiitch.nzbox.contact.compute.ContactResolver;
import com.github.fabiitch.nzbox.contact.compute.ShapeContact;

public class RectangleShape extends BodyShape<Rectangle> {
    public RectangleShape(Rectangle shape) {
        super(shape);
    }

    public RectangleShape(float width, float height) {
        super(new Rectangle(0, 0, width, height));
    }

    @Override
    public Vector2 getPosition(Vector2 res) {
        return RectangleUtils.getCenter(shape);
    }

    @Override
    public void setPosition(float x, float y) {
        RectangleUtils.setPosWithCenter(shape, x, y);
    }

    @Override
    public void setRotation(float rotation) {

    }

    @Override
    public void rotate(float amount) {
        //cant rotate
    }

    @Override
    public void scale(float scale) {
        RectangleUtils.scale(shape, scale);
    }

    @Override
    public void debugDraw(NzShapeRenderer shapeRenderer) {
        shapeRenderer.rect(shape);
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
        this.boundingRect.set(this.shape);
    }
}
