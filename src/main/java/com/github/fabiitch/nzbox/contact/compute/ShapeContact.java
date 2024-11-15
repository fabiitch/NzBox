package com.github.fabiitch.nzbox.contact.compute;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public interface ShapeContact {

    boolean testContact(Circle circle);

    Vector2 replace(Circle circle);

    boolean testContact(Rectangle rectangle);

    Vector2 replace(Rectangle rectangle);

    boolean testContact(Polygon polygon);

    Vector2 replace(Polygon polygon);
}
