package com.github.fabiitch.nzbox.utils;

public interface IdGenerator {

     int newId();

     void free(int id);
}
