package com.github.fabiitch.nzbox.profiler;

import com.github.fabiitch.nz.java.utils.IntCounter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BoxProfilerValue {
    private String value;
    public IntCounter step = new IntCounter();
    public IntCounter iteration = new IntCounter();

    public void startStep() {
        step.currentValue = 0;
    }

    public void inc() {
        iteration.currentValue++;
    }

    public void startIteration() {
        iteration.currentValue = 0;
    }

    public void endIteration() {
        int iterationValue = iteration.currentValue;
        step.currentValue += iterationValue;
        iteration.put(iterationValue);
    }

    public void endStep() {
        this.step.putCurrent();
    }


    public String toString(String fieldName) {
        return fieldName + "{" +
                "step=" + step.toString() +
                ", iteration=" + iteration.toString() +
                '}';
    }

    @Override
    public String toString() {
        return "BoxProfilerValue{" +
                "step=" + step.toString() +
                ", iteration=" + iteration.toString() +
                '}';
    }

    public String toStringValueAverage(String fieldName) {
        return fieldName + "{" +
                "step=" + step.toStringValueAverage() +
                ", iteration=" + iteration.toStringValueAverage() +
                '}';
    }

    public String toStringValueAverage() {
        return "BoxProfilerValue{" +
                "step=" + step.toStringValueAverage() +
                ", iteration=" + iteration.toStringValueAverage() +
                '}';
    }
}
