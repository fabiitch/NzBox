package com.github.fabiitch.nzbox.contact.compute.impl;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.github.fabiitch.nz.java.math.shapes.Segment;
import com.github.fabiitch.nz.java.math.shapes.intersectors.IntersectorCircle;
import com.github.fabiitch.nz.java.math.shapes.utils.CircleUtils;
import com.github.fabiitch.nz.java.math.vectors.V2;
import com.github.fabiitch.nzbox.contact.compute.ShapeContact;
import lombok.Setter;


public class ContactCircle implements ShapeContact {

    @Setter
    private Circle myCircle;

    private final Vector2 tmp = new Vector2();
    private final Vector2 tmp2 = new Vector2();
    private final Vector2 tmp3 = new Vector2();
    private final Segment tmpSegment = new Segment();

    @Override
    public boolean testContact(Circle circle) {
        float dst2 = tmp.set(circle.x, circle.y).dst2(myCircle.x, myCircle.y);
        return dst2 <= (myCircle.radius + circle.radius) * (myCircle.radius + circle.radius);
    }

    @Override
    public Vector2 replace(Circle circle) {
        Vector2 circleACenter = CircleUtils.getCenter(myCircle, tmp);
        float dst = myCircle.radius + circle.radius - tmp.dst(circle.x, circle.y);

        Vector2 circleBCenter = CircleUtils.getCenter(circle, tmp2);
        Vector2 dirToA = V2.directionTo(circleBCenter, circleACenter, tmp3);
        if (dirToA.isZero())
            dirToA.setToRandomDirection();
        dirToA.setLength(dst);

        Vector2 add = circleACenter.add(dirToA);
        return add;
    }


    @Override
    public boolean testContact(Rectangle rectangle) {
        return IntersectorCircle.overlapStickRectangle(myCircle, rectangle);
    }


    @Override
    public Vector2 replace(Rectangle rectangle) {
        IntersectorCircle.replaceFromRectangle(myCircle, rectangle, tmp);

        CircleUtils.getCenter(myCircle, tmp2);
        tmp2.add(tmp);
        return tmp2;
    }

    @Override
    public boolean testContact(Polygon polygon) { //TODO THREAD
        return IntersectorCircle.polygon(myCircle, polygon);
    }

    @Override
    public Vector2 replace(Polygon polygon) {
        IntersectorCircle.replaceFromPolygon(myCircle, polygon, tmp);
        CircleUtils.getCenter(myCircle, tmp2);
        tmp2.add(tmp);
        return tmp2;
    }

}
