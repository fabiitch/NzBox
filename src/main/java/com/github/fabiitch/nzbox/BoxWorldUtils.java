package com.github.fabiitch.nzbox;

import com.github.fabiitch.nzbox.bodies.BodyType;
import com.github.fabiitch.nzbox.data.Body;

public class BoxWorldUtils {

    public static boolean shouldUpdateBody(Body body) {
        if (!body.isActive())
            return false;
        if (!body.isDirty())
            return false;
        if (body.getBodyType() == BodyType.Static) return false;
        return true;
    }
}
