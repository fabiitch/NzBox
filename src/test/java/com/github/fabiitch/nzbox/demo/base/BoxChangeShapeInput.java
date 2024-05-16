package com.github.fabiitch.nzbox.demo.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.github.fabiitch.nzbox.data.Body;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class BoxChangeShapeInput extends InputAdapter {

    private BoxMvtInput mvtInput;

    private List<Body> bodyList = new ArrayList<>();

    private int bodyNumber = 0;

    public BoxChangeShapeInput(BoxMvtInput mvtInput, Body... bodies) {
        this.mvtInput = mvtInput;
        bodyList.addAll(Arrays.asList(bodies));
updateBodyControl();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.C) {
            bodyNumber += 1;
            updateBodyControl();
        } else if (keycode == Input.Keys.V) {
            bodyNumber -= 1;
            updateBodyControl();
        }
        return super.keyDown(keycode);
    }

    public void updateBodyControl() {
        if (bodyNumber < 0)
            bodyNumber = bodyList.size() - 1;
        else if (bodyNumber >= bodyList.size())
            bodyNumber = 0;

        if (!bodyList.isEmpty()) {
            Body body = bodyList.get(bodyNumber);
            mvtInput.setBody(body);
            Gdx.app.log("ChangeBodyControl", body.getUserData() != null ? body.getUserData().toString() : body.getId()+"");
        }

    }


}
