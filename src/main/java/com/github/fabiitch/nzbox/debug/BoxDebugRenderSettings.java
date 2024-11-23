package com.github.fabiitch.nzbox.debug;

import com.badlogic.gdx.graphics.Color;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoxDebugRenderSettings {

    private Color colorStatic, colorDynamic, colorPhantom, colorKinematic;

    private boolean drawInactive, drawVelocity;

    private boolean drawQuad, drawQuadData;
    private Color quadColor, quadDataColor;


    private boolean drawBodyPosition, drawBodyUserData;

}
