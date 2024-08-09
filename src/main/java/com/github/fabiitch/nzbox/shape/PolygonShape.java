package com.github.fabiitch.nzbox.shape;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.github.fabiitch.nz.gdx.render.NzShapeRenderer;
import com.github.fabiitch.nz.java.math.shapes.utils.PolygonUtils;
import com.github.fabiitch.nzbox.contact.compute.ContactResolver;
import com.github.fabiitch.nzbox.contact.compute.ShapeContact;

public class PolygonShape extends BodyShape<Polygon> {
    public PolygonShape(Polygon shape) {
        super(shape);
    }

    public PolygonShape(float[] vertices) {
        super(new Polygon(vertices));
        PolygonUtils.ensureClockWise(this.shape);
        //TODO not thread safe
        this.boundingRect.set(shape.getBoundingRectangle());
        if (!PolygonUtils.isConvex(shape))
            throw new GdxRuntimeException("NztBox, PolygonShape allow only convex polygons");
    }

    @Override
    public Vector2 getPosition(Vector2 res) {
        return res.set(shape.getX(), shape.getY());
    }

    @Override
    public void setPosition(float x, float y) {
        shape.setPosition(x, y);
    }

    @Override
    public void setRotation(float rotation) {
        shape.setRotation(rotation);
    }

    @Override
    public void rotate(float amount) {
        shape.rotate(amount);
    }

    @Override
    public void scale(float scale) {
        shape.scale(scale);
    }

    @Override
    public void debugDraw(NzShapeRenderer shapeRenderer) {
        shapeRenderer.polygon(shape);
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
        //TODO not thread safe
        this.boundingRect.set(shape.getBoundingRectangle());
    }
}
