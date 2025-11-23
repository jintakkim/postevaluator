package com.jintakkim.postevaluator.core;

import java.util.List;

public interface UserGenerator {
    List<User> generate(int count);
    String getModelName();
}
