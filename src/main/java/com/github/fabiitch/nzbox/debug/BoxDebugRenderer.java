package com.github.fabiitch.nzbox.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.github.fabiitch.nz.gdx.render.g2d.FontDrawer;
import com.github.fabiitch.nz.gdx.render.shape.Frustum2D;
import com.github.fabiitch.nz.gdx.render.shape.NzShapeRenderer;
import com.github.fabiitch.nz.java.data.quadtree.QuadTreeRenderer;
import com.github.fabiitch.nzbox.BoxWorld;
import com.github.fabiitch.nzbox.bodies.BodyType;
import com.github.fabiitch.nzbox.data.Body;
import com.github.fabiitch.nzbox.data.Fixture;

public class BoxDebugRenderer {

    private final NzShapeRenderer shapeRenderer;
    private final SpriteBatch spriteBatch;
    private final BitmapFont bitmapFont;

    private final FontDrawer fontDrawer;

    private final Frustum2D frustum;

    private boolean drawQuad = true;

    private final Vector2 tmp1 = new Vector2(), tmp2 = new Vector2(), tmp3 = new Vector2();

    private final QuadTreeRenderer quadTreeRenderer;

    public BoxDebugRenderer() {
        shapeRenderer = new NzShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        bitmapFont = new BitmapFont();
        bitmapFont.getData().setScale(0.5f);
        spriteBatch = new SpriteBatch();
        quadTreeRenderer = new QuadTreeRenderer();
        frustum = new Frustum2D();

        fontDrawer = new FontDrawer(bitmapFont, spriteBatch);
    }

    public void render(BoxWorld world, OrthographicCamera camera) {
        frustum.update(camera);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        Array<Body> bodies = world.getData().getBodies();

        for (int i1 = 0, n1 = bodies.size; i1 < n1; i1++) {
            Body body = bodies.get(i1);

            Array<Fixture> fixtures = body.getFixtures();
            for (int i2 = 0, n2 = fixtures.size; i2 < n2; i2++) {
                Fixture fixture = fixtures.get(i2);

                if (!frustum.isInside(fixture.getBodyShape().getBoundingRect()))
                    continue;

                if (body.getBodyType() == BodyType.Static)
                    shapeRenderer.setColor(Color.RED);
                else if (body.getBodyType() == BodyType.Dynamic)
                    shapeRenderer.setColor(Color.BLUE);

                shapeRenderer.filled();
                shapeRenderer.circle(fixture.getPosition(tmp1), 1);

                shapeRenderer.line();
                fixture.getBodyShape().debugDraw(shapeRenderer);
            }

            shapeRenderer.setColor(Color.GOLD);
            tmp1.set(body.getPosition());
            tmp2.set(body.getVelocity());

            shapeRenderer.line();
            shapeRenderer.line(tmp1, tmp3.set(tmp1).add(tmp2.scl(0.2f)));
        }

        if (drawQuad) {
            quadTreeRenderer.render(world.getData().getQuadTree(), camera);
        }
        shapeRenderer.end();

        spriteBatch.begin();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.end();

    }
}
