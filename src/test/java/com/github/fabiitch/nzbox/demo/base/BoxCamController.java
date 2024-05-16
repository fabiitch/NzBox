package com.github.fabiitch.nzbox.demo.base;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import lombok.Getter;

public class BoxCamController extends InputAdapter {

    @Getter
    private final Vector2 direction = new Vector2();
    @Getter
    private final Vector2 mouseScreenPos = new Vector2();
    @Getter
    private final Vector2 mouseWorldPos = new Vector2();
    @Getter
    private float scroll = 0;

    private final OrthographicCamera camera;

    public BoxCamController(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mouseScreenPos.set(screenX, screenY);

        Vector3 unproject = camera.unproject(new Vector3(screenX, screenY, 0));
        mouseWorldPos.set(unproject.x, unproject.y);
        return super.mouseMoved(screenX, screenY);
    }


    public void update(float dt) {
        Vector2 camMove = direction.cpy().nor().setLength(100).scl(dt);

        camera.position.x += camMove.x;
        camera.position.y += camMove.y;

        if (scroll == 0)
            camera.zoom = 1;
        else if (scroll > 0)
            camera.zoom = scroll;
        else
            camera.zoom = 1 / Math.abs(scroll);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.RIGHT) {
            direction.x += 1;
        } else if (keycode == Input.Keys.LEFT) {
            direction.x -= 1;
        } else if (keycode == Input.Keys.UP) {
            direction.y += 1;
        } else if (keycode == Input.Keys.DOWN) {
            direction.y -= 1;
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.RIGHT) {
            direction.x -= 1;
        } else if (keycode == Input.Keys.LEFT) {
            direction.x += 1;
        } else if (keycode == Input.Keys.UP) {
            direction.y -= 1;
        } else if (keycode == Input.Keys.DOWN) {
            direction.y += 1;
        }
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        scroll += amountY;
        return true;
    }


}
