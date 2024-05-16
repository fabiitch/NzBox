package com.github.fabiitch.nzbox.bodies;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BodyDef {

    private BodyType bodyType;
    private boolean bullet;
    private boolean canRotate = true;
}
