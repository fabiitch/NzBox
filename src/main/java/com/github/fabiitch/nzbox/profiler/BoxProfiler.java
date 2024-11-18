package com.github.fabiitch.nzbox.profiler;

import com.badlogic.gdx.math.FloatCounter;
import com.badlogic.gdx.utils.Array;
import com.github.fabiitch.nz.java.utils.IntCounter;
import lombok.Getter;

@Getter
public class BoxProfiler {
    public IntCounter iterationPerStep = new IntCounter();

    public FloatCounter deltaTime, timerStep, timerIteration;
    public BoxProfilerValue bodyMove, bodyCheckCollision, fixtureCheckCollision;
    public BoxProfilerValue beginContact, endContact;

    public BoxProfilerValue testContact, replaceContact, collisionData, computeContact;

    private final Array<BoxProfilerValue> profilerValues;

    private long timeStartStep, timeStartIteration;

    public BoxProfiler() {
        bodyMove = new BoxProfilerValue();
        bodyCheckCollision = new BoxProfilerValue();
        fixtureCheckCollision = new BoxProfilerValue();
        beginContact = new BoxProfilerValue();
        endContact = new BoxProfilerValue();
        testContact = new BoxProfilerValue();
        replaceContact = new BoxProfilerValue();
        collisionData = new BoxProfilerValue();
        computeContact = new BoxProfilerValue();

        profilerValues = new Array<>();
        profilerValues.addAll(bodyCheckCollision, bodyCheckCollision, fixtureCheckCollision);
        profilerValues.addAll(beginContact, endContact);
        profilerValues.addAll(testContact, replaceContact, collisionData, computeContact);
    }

    public void startStep(float dt) {
        iterationPerStep.currentValue = 0;

        deltaTime.put(dt);
        timeStartStep = System.nanoTime();

        for (int i = 0, n = profilerValues.size; i < n; i++)
            profilerValues.get(i).startStep();
    }


    public void endStep() {
        long endTime = System.nanoTime();
        timerStep.put(endTime - timeStartStep);
        iterationPerStep.putCurrent();

        for (int i = 0, n = profilerValues.size; i < n; i++)
            profilerValues.get(i).endStep();
    }

    public void startIteration() {
        timeStartIteration = System.nanoTime();
        iterationPerStep.currentValue++;

        for (int i = 0, n = profilerValues.size; i < n; i++)
            profilerValues.get(i).startIteration();
    }

    public void endIteration() {
        for (int i = 0, n = profilerValues.size; i < n; i++)
            profilerValues.get(i).endIteration();

        long endTime = System.nanoTime();
        timerIteration.put(endTime - timeStartIteration);
    }

}
