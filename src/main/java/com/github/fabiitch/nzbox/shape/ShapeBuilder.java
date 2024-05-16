package com.github.fabiitch.nzbox.shape;

import com.badlogic.gdx.math.Rectangle;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ShapeBuilder {

    public static Rectangle[] getRectsAround(Rectangle rect, float sizeRect) {
        Rectangle[] rects = new Rectangle[4];

        Rectangle rectBot = new Rectangle(rect.x, rect.y - rect.height / 2 - sizeRect / 2, rect.width, sizeRect);
        rects[0] = rectBot;

        Rectangle rectTop = new Rectangle(rect.x, rect.y + rect.height / 2 + sizeRect / 2, rect.width, sizeRect);
        rects[1] = rectTop;

        Rectangle rectLeft = new Rectangle(rect.x - sizeRect / 2 - rect.getWidth() / 2, rect.y, sizeRect, rect.getHeight());
        rects[2] = rectLeft;

        Rectangle rectRight = new Rectangle(rect.x + rect.getWidth() / 2 + sizeRect / 2, rect.y, sizeRect, rect.getHeight());
        rects[3] = rectRight;
        return rects;
    }

}
