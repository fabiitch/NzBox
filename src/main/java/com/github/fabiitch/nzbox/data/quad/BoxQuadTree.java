package com.github.fabiitch.nzbox.data.quad;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.github.fabiitch.nz.java.data.quadtree.QuadTree;
import com.github.fabiitch.nz.java.math.shapes.utils.RectangleUtils;
import com.github.fabiitch.nzbox.data.Fixture;
import com.github.fabiitch.nzbox.pools.BoxPools;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BoxQuadTree {
    private final QuadTree<Fixture<?>> quadTree = new QuadTree<>();
    private final IntMap<QuadTree<Fixture<?>>> fixtureMap = new IntMap<>();

    private final BoxPools pools;


    public void addFixture(Fixture fixture) {
        QuadTree<Fixture<?>> quad = quadTree.add(fixture, fixture.getBodyShape().getBoundingRect());
        fixtureMap.put(fixture.getId(), quad);

    }

    public void remove(Fixture fixture) {
        quadTree.remove(fixture);
        fixtureMap.remove(fixture.getId());
    }

    public void update(Fixture fixture) {
        Rectangle fixtureRect = fixture.getBodyShape().getBoundingRect();
        QuadTree<Fixture<?>> oldQuad = fixtureMap.get(fixture.getId());
        if (!RectangleUtils.containsStick(oldQuad.boundingRect, fixtureRect)) {

        }
    }

    public Array<Fixture<?>> query(Rectangle boundingRect) {
        Array<Fixture<?>> fixtureArray = pools.getFixtureArray();
        return quadTree.query(boundingRect, fixtureArray);
    }

    public void updateQuad() {
        quadTree.update();
    }
}
