package com.github.fabiitch.nzbox.contact.compute.normal;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.github.fabiitch.nz.java.math.shapes.intersectors.IntersectorCircle;
import com.github.fabiitch.nz.java.math.shapes.intersectors.IntersectorPolygon;
import com.github.fabiitch.nz.java.math.shapes.utils.PolygonUtils;
import com.github.fabiitch.nz.java.math.vectors.V2;
import com.github.fabiitch.nzbox.contact.compute.ShapeContact;
import lombok.Setter;

public class ContactPolygon implements ShapeContact {
    @Setter
    private Polygon myPolygon;
    private final Vector2 tmp = new Vector2(), tmp2 = new Vector2();

    @Override
    public boolean testContact(Circle circle) {
        return IntersectorPolygon.circle(circle, myPolygon);
    }

    @Override
    public Vector2 replace(Circle circle) {
        IntersectorCircle.replaceFromPolygon(circle, myPolygon, tmp);
        Vector2 polygonPos = PolygonUtils.getPos(myPolygon, tmp2);
        V2.inv(tmp);
        polygonPos.add(tmp);

        return polygonPos;
    }

    @Override
    public boolean testContact(Rectangle rectangle) {
        return IntersectorPolygon.rectangle(myPolygon, rectangle);
    }

    @Override
    public Vector2 replace(Rectangle rectangle) {
        boolean overlaps = IntersectorPolygon.rectangle(myPolygon, rectangle, IntersectorPolygon.tmpTranslationVector);
        if (overlaps) {
            tmp.set(myPolygon.getX(), myPolygon.getY());
            tmp2.set(IntersectorPolygon.tmpTranslationVector.normal).setLength(IntersectorPolygon.tmpTranslationVector.depth);
            tmp.add(tmp2);
            return tmp;
        }
        return rectangle.getPosition(tmp);
    }

    @Override
    public boolean testContact(Polygon polygon) {
        return IntersectorPolygon.polygons(polygon, myPolygon, null);
    }

    @Override
    public Vector2 replace(Polygon polygon) {
        boolean overlaps = IntersectorPolygon.polygons(myPolygon, polygon, IntersectorPolygon.tmpTranslationVector);
        if (overlaps) {
            Vector2 posPolygon = PolygonUtils.getPos(myPolygon, tmp);
            tmp2.set(IntersectorPolygon.tmpTranslationVector.normal).setLength(IntersectorPolygon.tmpTranslationVector.depth);
            posPolygon.add(tmp2);
            return tmp2;
        }
        return PolygonUtils.getPos(polygon, tmp);
    }
}
