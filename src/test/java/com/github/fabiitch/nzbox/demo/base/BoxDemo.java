package com.github.fabiitch.nzbox.demo.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.fabiitch.nz.gdx.debug.huddebug.HudDebug;
import com.github.fabiitch.nz.gdx.debug.huddebug.internal.HudDebugPosition;
import com.github.fabiitch.nz.gdx.scene2D.nz.NzStage;
import com.github.fabiitch.nzbox.BoxWorld;
import com.github.fabiitch.nzbox.debug.BoxDebugRenderer;
import lombok.Getter;


public abstract class BoxDemo implements Screen {

    @Getter
    protected BoxWorld world;
    protected BoxDebugRenderer boxDebugRenderer;
    protected OrthographicCamera camera;

    protected InputMultiplexer inputMultiplexer = new InputMultiplexer();
    protected BoxCamController camController;
    protected BoxMvtInput mvtInput = new BoxMvtInput();

    protected NzStage stage;
    protected HudDebug hudDebug;
    protected Skin skin;

    public BoxDemo() {
        stage = new NzStage();
        skin = new Skin(Gdx.files.internal("skins/ui/uiskin.json"));
        hudDebug = new HudDebug(stage, skin);

        boxDebugRenderer = new BoxDebugRenderer();

        world = new BoxWorld();
        camera = new OrthographicCamera(800, 600);
        camController = new BoxCamController(camera);

        Gdx.input.setInputProcessor(inputMultiplexer);
        inputMultiplexer.addProcessor(camController);
        inputMultiplexer.addProcessor(mvtInput);

        HudDebug.add("Cam Pos", camera.position, HudDebugPosition.TOP_LEFT);
        HudDebug.add("Cam Dir", camController.getDirection(), HudDebugPosition.TOP_LEFT);
        HudDebug.add("Cam Zoom", camera.zoom, HudDebugPosition.TOP_LEFT);
        HudDebug.add("Mouse Screen Pos", camController.getMouseScreenPos(), HudDebugPosition.TOP_LEFT);
        HudDebug.add("Mouse World Pos", camController.getMouseWorldPos(), HudDebugPosition.TOP_LEFT);
    }

    @Override
    public void render(float delta) {
        HudDebug.update("Cam Pos", camera.position);
        HudDebug.update("Cam Dir", camController.getDirection());
        HudDebug.update("Cam Zoom", camera.zoom);
        HudDebug.update("Mouse Screen Pos", camController.getMouseScreenPos());
        HudDebug.update("Mouse World Pos", camController.getMouseWorldPos());

        Gdx.graphics.setTitle(this.getClass().getSimpleName() + " FPS:" + Gdx.graphics.getFramesPerSecond());
        ScreenUtils.clear(Color.BLACK, true);

        mvtInput.update();
        world.step(delta);


        camController.update(delta);
        camera.update();
        boxDebugRenderer.render(world, camera.combined);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
