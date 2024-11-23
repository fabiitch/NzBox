package com.github.fabiitch.nzbox.data.quad;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.github.fabiitch.nz.java.data.quadtree.QuadTree;
import com.github.fabiitch.nzbox.data.Fixture;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BoxQuadTree {
    @Getter
    private final QuadTree<Fixture<?>> quadTree;
    private final IntMap<QuadTree<Fixture<?>>> fixtureMap = new IntMap<>();

    public void addFixture(Fixture fixture) {
        QuadTree<Fixture<?>> quad = quadTree.add(fixture, fixture.getBodyShape().getBoundingRect());
        fixtureMap.put(fixture.getId(), quad);
    }

    public void remove(Fixture fixture) {
        boolean remove = quadTree.remove(fixture);
        fixtureMap.remove(fixture.getId());
    }

    public void update(Fixture fixture) {
        quadTree.update(fixture);

//        QuadTree<Fixture<?>> fixtureQuadTree = fixtureMap.get(fixture.getId());
//        QuadTree update = fixtureQuadTree.update(fixture);
//        if(fixtureQuadTree != update){
//            fixtureMap.put(fixture.getId(), update);
//        }
    }
    public void updateQuad() {
        quadTree.recompute();
    }

    public void getFixtureClose(Fixture fixtureA, Array<Fixture<?>> results) {
        QuadTree<Fixture<?>> fixtureQuadTree = fixtureMap.get(fixtureA.getId());
        fixtureQuadTree.getAllValuesAndParents(results);
    }
}
