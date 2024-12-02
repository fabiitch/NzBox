package com.github.fabiitch.nzbox.contact.compute;


import com.github.fabiitch.nzbox.contact.compute.normal.ContactCircle;
import com.github.fabiitch.nzbox.contact.compute.normal.ContactPolygon;
import com.github.fabiitch.nzbox.contact.compute.normal.ContactRectangle;
import com.github.fabiitch.nzbox.shape.CircleShape;
import com.github.fabiitch.nzbox.shape.PolygonShape;
import com.github.fabiitch.nzbox.shape.RectangleShape;

public class ContactResolver {

    private final ContactCircle contactCircle = new ContactCircle();
    private final ContactRectangle contactRectangle = new ContactRectangle();
    private final ContactPolygon contactPolygon = new ContactPolygon();


    public ShapeContact get(CircleShape circleShape) {
        contactCircle.setMyCircle(circleShape.getShape());
        return contactCircle;
    }

    public ShapeContact get(RectangleShape rectangleShape) {
        contactRectangle.setMyRectangle(rectangleShape.getShape());
        return contactRectangle;
    }

    public ShapeContact get(PolygonShape polygonShape) {
        contactPolygon.setMyPolygon(polygonShape.getShape());
        return contactPolygon;
    }
}
