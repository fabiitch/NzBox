package com.github.fabiitch.nzbox.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.fabiitch.nz.gdx.render.shape.NzShapeRenderer;
import com.github.fabiitch.nzbox.data.Body;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoxDebugRenderConfig {

    private Color colorStatic = Color.GRAY;
    private Color colorDynamic = Color.BLUE;
    private Color colorPhantom = Color.WHITE;
    private Color colorKinematic = Color.WHITE;

    private boolean drawVelocity = true;
    private Color colorVelocity = Color.GOLD;

    private boolean drawAABB = false;
    private Color colorAABB = Color.GREEN;

    private boolean drawCenter = true;
    private Color colorCenter = Color.RED;

    private boolean drawBodyUserData;

    private boolean drawQuadTree = true;
    private boolean drawQuadTreeData = true;

    private Color colorQuad = Color.PURPLE;
    private Color colorQuadsData = Color.WHITE;

    private NzShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;
    private BitmapFont fontUserData;
    private BitmapFont fontQuadTreeData;

    public BoxDebugRenderConfig setColorStatic(Color colorStatic) {
        this.colorStatic = colorStatic;
        return this;
    }

    public BoxDebugRenderConfig setColorDynamic(Color colorDynamic) {
        this.colorDynamic = colorDynamic;
        return this;
    }

    public BoxDebugRenderConfig setColorPhantom(Color colorPhantom) {
        this.colorPhantom = colorPhantom;
        return this;
    }

    public BoxDebugRenderConfig setColorKinematic(Color colorKinematic) {
        this.colorKinematic = colorKinematic;
        return this;
    }

    public BoxDebugRenderConfig setDrawVelocity(boolean drawVelocity) {
        this.drawVelocity = drawVelocity;
        return this;
    }

    public BoxDebugRenderConfig setColorVelocity(Color colorVelocity) {
        this.colorVelocity = colorVelocity;
        return this;
    }

    public BoxDebugRenderConfig setDrawAABB(boolean drawAABB) {
        this.drawAABB = drawAABB;
        return this;
    }

    public BoxDebugRenderConfig setColorAABB(Color colorAABB) {
        this.colorAABB = colorAABB;
        return this;
    }

    public BoxDebugRenderConfig setDrawCenter(boolean drawCenter) {
        this.drawCenter = drawCenter;
        return this;
    }

    public BoxDebugRenderConfig setColorCenter(Color colorCenter) {
        this.colorCenter = colorCenter;
        return this;
    }

    public BoxDebugRenderConfig setDrawBodyUserData(boolean drawBodyUserData) {
        this.drawBodyUserData = drawBodyUserData;
        return this;
    }


    public BoxDebugRenderConfig setDrawQuadTree(boolean drawQuadTree) {
        this.drawQuadTree = drawQuadTree;
        return this;
    }

    public BoxDebugRenderConfig setDrawQuadTreeData(boolean drawQuadTreeData) {
        this.drawQuadTreeData = drawQuadTreeData;
        return this;
    }

    public BoxDebugRenderConfig setColorQuad(Color colorQuad) {
        this.colorQuad = colorQuad;
        return this;
    }

    public BoxDebugRenderConfig setColorQuadsData(Color colorQuadsData) {
        this.colorQuadsData = colorQuadsData;
        return this;
    }

    public BoxDebugRenderConfig setShapeRenderer(NzShapeRenderer shapeRenderer) {
        this.shapeRenderer = shapeRenderer;
        return this;
    }

    public BoxDebugRenderConfig setSpriteBatch(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        return this;
    }

    public BoxDebugRenderConfig setFontUserData(BitmapFont fontUserData) {
        this.fontUserData = fontUserData;
        return this;
    }

    public BoxDebugRenderConfig setFontQuadTreeData(BitmapFont fontQuadTreeData) {
        this.fontQuadTreeData = fontQuadTreeData;
        return this;
    }

    public Color getColor(Body body) {
        switch (body.getBodyType()) {
            case Dynamic:
                return colorDynamic;
            case Static:
                return colorStatic;
            case Kinematic:
                return colorKinematic;
            case Phantom:
                return colorPhantom;
        }
        return colorDynamic;
    }
}
