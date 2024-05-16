package com.github.fabiitch.nzbox.demo;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.github.fabiitch.nz.java.math.shapes.builders.PolygonBuilder;
import com.github.fabiitch.nzbox.bodies.BodyType;
import com.github.fabiitch.nzbox.data.Body;
import com.github.fabiitch.nzbox.data.Fixture;
import com.github.fabiitch.nzbox.demo.base.BoxChangeShapeInput;
import com.github.fabiitch.nzbox.demo.base.BoxDemo;
import com.github.fabiitch.nzbox.shape.CircleShape;
import com.github.fabiitch.nzbox.shape.PolygonShape;
import com.github.fabiitch.nzbox.shape.RectangleShape;
import com.github.fabiitch.nzbox.shape.ShapeBuilder;

public class DemoAllShape extends BoxDemo {
    Body circle;
    Body rectangle;
    Body polygonRect;

    BoxChangeShapeInput changeShapeInput;

    @Override
    public void show() {
        circle = createCircle(0, 0, 50, BodyType.Dynamic, "circle");
        rectangle = createRectangle(new Rectangle(200, 200, 100, 50), BodyType.Dynamic, "rect");
        polygonRect = createPolygon(PolygonBuilder.get(0, 0, 50, 50, 100, 0, 50, -50), BodyType.Dynamic, "polygon ");

        changeShapeInput = new BoxChangeShapeInput(mvtInput, circle, rectangle, polygonRect);
        inputMultiplexer.addProcessor(changeShapeInput);

        Rectangle start = new Rectangle(0, 0, 600, 600);
        Rectangle[] rectsAround = ShapeBuilder.getRectsAround(start, 10);

        for (Rectangle rectangle : rectsAround) {
            if (rectangle != null)
                createRectangle(rectangle, BodyType.Static, "Wall");
        }
    }

    @Override
    public void render(float delta) {
        circle.getVelocity().set(camController.getDirection().cpy().scl(10));
        super.render(delta);
    }


    public Body createRectangle(Rectangle rectangle, BodyType bodyType, String userData) {
        Body body = new Body(bodyType);
        world.addBody(body);
        body.setUserData(userData);

        Fixture fixture = new Fixture(new RectangleShape(rectangle.getWidth(), rectangle.getHeight()));

        body.addFixture(fixture);

        body.setPosition(rectangle.x, rectangle.y, 0);

        return body;
    }

    public Body createPolygon(Polygon polygon, BodyType bodyType, String userData) {
        Body body = new Body(bodyType);
        world.addBody(body);
        body.setUserData(userData);

        Fixture fixture = new Fixture(new PolygonShape(polygon));

        body.addFixture(fixture);

        body.setPosition(polygon.getX(), polygon.getY(), 0);

        return body;
    }


    public Body createCircle(float x, float y, float radius, BodyType bodyType, String userData) {
        Body body = new Body(bodyType);
        world.addBody(body);
        body.setUserData(userData);

        Fixture fixture = new Fixture(new CircleShape(radius));
        body.setPosition(x, y, 0);
        body.addFixture(fixture);

        return body;
    }
}
