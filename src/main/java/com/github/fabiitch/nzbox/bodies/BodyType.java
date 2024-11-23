package com.github.fabiitch.nzbox.bodies;

public enum BodyType  {
    Dynamic, //collides with all
    Static,  //collides with all except kinematic
    Kinematic,  //collides only with kinematic
    Phantom // never collides
}
