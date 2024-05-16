package com.github.fabiitch.nzbox.demo.base;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.github.fabiitch.nzbox.data.Body;
import lombok.Getter;
import lombok.Setter;

public class BoxMvtInput extends InputAdapter {

    @Getter
    private final Vector2 direction = new Vector2();

    @Getter
    @Setter
    private Body body;

    public BoxMvtInput() {
        this.body = body;
    }

    public void update() {
        if (body != null)
            body.getVelocity().set(direction.cpy().scl(30));
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.W) {
            direction.y += 1;
        } else if (keycode == Input.Keys.S) {
            direction.y -= 1;
        } else if (keycode == Input.Keys.D) {
            direction.x += 1;
        } else if (keycode == Input.Keys.A) {//q in azerty
            direction.x -= 1;
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.W) {
            direction.y -= 1;
        } else if (keycode == Input.Keys.S) {
            direction.y += 1;
        } else if (keycode == Input.Keys.D) {
            direction.x -= 1;
        } else if (keycode == Input.Keys.A) {//q in azerty
            direction.x += 1;
        }
        return true;
    }
}
