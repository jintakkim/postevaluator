package com.jintakkim.postevaluator.test;

import com.jintakkim.postevaluator.persistance.JdbiContext;

@FunctionalInterface
public interface Executable {
    void execute(JdbiContext jdbiContext);
}
