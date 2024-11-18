package com.github.fabiitch.nzbox.data.quad;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.github.fabiitch.nz.java.data.quadtree.QuadTree;
import com.github.fabiitch.nzbox.data.Fixture;

public class BoxQuadTree {
    private QuadTree<Fixture<?>> quadTree = new QuadTree<>();
    private IntMap<QuadTree<Fixture<?>>> fixtureMap = new IntMap<>();


    public void addFixture(Fixture fixture){
        QuadTree<Fixture<?>> quad = quadTree.add(fixture, fixture.getBodyShape().getBoundingRect());
        fixtureMap.put(fixture.getId(), quad);

    }

    public void remove(Fixture fixture) {
        quadTree.remove(fixture);
        fixtureMap.remove(fixture.getId());
    }

    public void update(Fixture fixture) {
        Rectangle boundingRect = fixture.getBodyShape().getBoundingRect();

        QuadTree<Fixture<?>> oldQuad = fixtureMap.get(fixture.getId());
    }

}
