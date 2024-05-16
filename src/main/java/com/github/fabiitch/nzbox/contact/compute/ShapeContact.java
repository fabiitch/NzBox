package com.github.fabiitch.nzbox.contact.compute;

import com.badlogic.gdx.math.*;

public interface ShapeContact {

    boolean testContact(Circle circle);

    Vector2 replace(Circle circle);

    boolean testContact(Rectangle rectangle);

    Vector2 replace(Rectangle rectangle);

    boolean testContact(Polygon polygon);

    Vector2 replace(Polygon polygon);
}
