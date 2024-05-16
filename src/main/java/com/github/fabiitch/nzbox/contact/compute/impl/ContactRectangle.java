package com.github.fabiitch.nzbox.contact.compute.impl;

import com.badlogic.gdx.math.*;
import com.github.fabiitch.nz.java.math.shapes.intersectors.IntersectorCircle;
import com.github.fabiitch.nz.java.math.shapes.intersectors.IntersectorPolygon;
import com.github.fabiitch.nz.java.math.shapes.intersectors.IntersectorRectangle;
import com.github.fabiitch.nz.java.math.shapes.utils.PolygonUtils;
import com.github.fabiitch.nz.java.math.shapes.utils.RectangleUtils;
import com.github.fabiitch.nz.java.math.vectors.V2;
import com.github.fabiitch.nzbox.contact.compute.ShapeContact;
import lombok.Setter;

public class ContactRectangle implements ShapeContact {
    @Setter
    private Rectangle myRectangle;
    private final Vector2 tmp1 = new Vector2(), tmp2 = new Vector2(), tmp3 = new Vector2();


    @Override
    public boolean testContact(Circle circle) {
        return IntersectorCircle.overlapStickRectangle(circle, myRectangle);
    }

    @Override
    public Vector2 replace(Circle circle) {
        IntersectorCircle.replaceFromRectangle(circle, myRectangle, tmp1);
        V2.inv(tmp1);
        RectangleUtils.getCenter(myRectangle, tmp2);
        tmp2.add(tmp1);
        return tmp2;
    }

    @Override
    public boolean testContact(Rectangle rectangle) {
        return RectangleUtils.overlapsStick(myRectangle, rectangle);
    }

    @Override
    public Vector2 replace(Rectangle rectangle) {
        Intersector.MinimumTranslationVector translationVector = IntersectorPolygon.tmpTranslationVector;
        boolean overlaps = IntersectorRectangle.rectangles(myRectangle, rectangle, translationVector);
        if (overlaps) {
            RectangleUtils.getCenter(myRectangle, tmp1);
            tmp2.set(IntersectorPolygon.tmpTranslationVector.normal).setLength(IntersectorPolygon.tmpTranslationVector.depth);
            tmp1.add(tmp2);
            return tmp1;
        }
        return rectangle.getPosition(tmp1);
    }

    @Override
    public boolean testContact(Polygon polygon) {
        return IntersectorPolygon.rectangle(polygon, myRectangle);
    }

    @Override
    public Vector2 replace(Polygon polygon) {
        Intersector.MinimumTranslationVector translationVector = IntersectorPolygon.tmpTranslationVector;
        boolean overlaps = IntersectorRectangle.polygon(myRectangle, polygon, translationVector);
        if (overlaps) {
            tmp1.set(RectangleUtils.getCenter(myRectangle, tmp1));
            tmp2.set(IntersectorPolygon.tmpTranslationVector.normal).setLength(IntersectorPolygon.tmpTranslationVector.depth);
            tmp1.add(tmp2);
            return tmp1;
        }
        return PolygonUtils.getPos(polygon, tmp1);
    }
}
