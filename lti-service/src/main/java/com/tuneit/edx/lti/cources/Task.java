package com.tuneit.edx.lti.cources;

import java.util.Random;

public interface Task {

    String getQuestion();

    default long getId() {
        return System.nanoTime();
    }

    default boolean isDone() {
        return new Random().nextBoolean();
    }

}
