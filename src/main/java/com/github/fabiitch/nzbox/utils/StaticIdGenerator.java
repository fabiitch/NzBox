package com.github.fabiitch.nzbox.utils;

public class StaticIdGenerator implements IdGenerator {
    private int start = 0;

    @Override
    public int newId() {
        start += 1;
        return start;
    }

    @Override
    public void free(int id) {

    }
}
