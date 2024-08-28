package com.github.fabiitch.nzbox.contact;

import com.github.fabiitch.nzbox.data.Body;
import com.github.fabiitch.nzbox.data.Fixture;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactFixture {
    private Fixture fixtureA;
    private Fixture fixtureB;

    private boolean replace;
    private boolean triggerEnd = true;

    public boolean hasBody(Body body) {
        return this.fixtureA.getBody() == body || this.fixtureB.getBody() == body;
    }
    public boolean hasFixture(Fixture fixture) {
        return this.fixtureA == fixture || this.fixtureB == fixture;
    }

    public Fixture getOther(Fixture<?> fixture) {
        if (fixture == fixtureA)
            return fixtureB;
        return fixtureA;
    }

}
