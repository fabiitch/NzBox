package com.github.fabiitch.nzbox.debug;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Bits;
import com.github.fabiitch.nz.gdx.render.g2d.FontDrawer;
import com.github.fabiitch.nz.gdx.render.shape.Frustum2D;
import com.github.fabiitch.nz.gdx.render.shape.NzShapeRenderer;
import com.github.fabiitch.nz.java.data.quadtree.render.QuadTreeRenderConfig;
import com.github.fabiitch.nz.java.data.quadtree.render.QuadTreeRenderer;
import com.github.fabiitch.nzbox.BoxWorld;
import com.github.fabiitch.nzbox.data.Body;
import com.github.fabiitch.nzbox.data.Fixture;
import lombok.Getter;

public class BoxDebugRenderer {
    @Getter
    private final BoxDebugRenderConfig config;
    private final NzShapeRenderer shapeRenderer;
    private final SpriteBatch spriteBatch;

    private BitmapFont fontBodyUserData = null;
    private BitmapFont fontQuadTreeData = null;
    private FontDrawer fontDrawer = null;

    private final Frustum2D frustum;


    private final Vector2 tmp1 = new Vector2(), tmp2 = new Vector2(), tmp3 = new Vector2();

    private QuadTreeRenderer quadTreeRenderer;

    public BoxDebugRenderer(BoxDebugRenderConfig config) {
        this.config = config;
        this.shapeRenderer = config.getShapeRenderer() != null ? config.getShapeRenderer() : new NzShapeRenderer();
        this.shapeRenderer.setAutoShapeType(true);

        this.spriteBatch = config.getSpriteBatch() != null ? config.getSpriteBatch() : new SpriteBatch();

        if (config.getFontUserData() == null && config.isDrawBodyUserData()) {
            fontBodyUserData = new BitmapFont();
            fontBodyUserData.getData().setScale(0.5f);
            fontDrawer = new FontDrawer(fontBodyUserData, spriteBatch);
        }
        if (config.getFontQuadTreeData() == null && config.isDrawQuadTreeData()) {
            fontQuadTreeData = new BitmapFont();
        }
        if (config.isDrawAABB() || config.isDrawQuadTree() || config.isDrawQuadTreeData()) {
            QuadTreeRenderConfig quadTreeRenderConfig = new QuadTreeRenderConfig()
                    .spriteBatch(spriteBatch)
                    .shapeRender(shapeRenderer)
                    .drawRectangles(config.isDrawAABB())
                    .colorRects(config.getColorAABB())
                    .drawQuads(config.isDrawQuadTree())
                    .colorQuad(config.getColorQuad())
                    .fontQuadsData(fontQuadTreeData);
            quadTreeRenderer = new QuadTreeRenderer(quadTreeRenderConfig);
        }
        frustum = new Frustum2D();
    }

    public BoxDebugRenderer() {
        this(new BoxDebugRenderConfig());
    }

    public void render(BoxWorld world, OrthographicCamera camera) {
        frustum.update(camera);
        Array<Fixture<?>> fixtures = world.getData().query(frustum.getRectangle());

        Array<Body> bodies = new Array<>();
        Bits bodyDraw = new Bits();

        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int i = 0, n = fixtures.size; i < n; i++) {
            final Fixture fixture = fixtures.get(i);
            Body body = fixture.getBody();

            shapeRenderer.setColor(config.getColor(body));
            fixture.getBodyShape().debugDraw(shapeRenderer);

            if (!bodyDraw.get(body.getId())) {
                bodyDraw.set(body.getId());
                bodies.add(body);
            }
        }

        if (config.isDrawCenter()) {
            shapeRenderer.filled();
            shapeRenderer.setColor(config.getColorCenter());
            for (int i = 0, n = bodies.size; i < n; i++) {
                Body body = bodies.get(i);
                shapeRenderer.circle(body.getPosition(), 1);
            }
        }
        if (config.isDrawVelocity()) {
            shapeRenderer.line();
            shapeRenderer.setColor(config.getColorVelocity());
            for (int i = 0, n = bodies.size; i < n; i++) {
                Body body = bodies.get(i);

                tmp1.set(body.getPosition());
                tmp2.set(body.getVelocity());
                shapeRenderer.line(tmp1, tmp3.set(tmp1).add(tmp2.scl(0.2f)));
            }
        }
        shapeRenderer.end();
        if (config.isDrawQuadTree()) {
            quadTreeRenderer.render(world.getData().getQuadTree(), camera);
        }

        if (config.isDrawBodyUserData()) {
            spriteBatch.begin();
            spriteBatch.setProjectionMatrix(camera.combined);
            for (Body body : bodies) {
                tmp1.set(body.getPosition()).add(0, 10);
                fontDrawer.drawAt(tmp1, body.getUserData().toString());
            }
            spriteBatch.end();
        }
    }
}
