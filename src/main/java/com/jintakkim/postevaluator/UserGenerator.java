package com.jintakkim.postevaluator;

import java.util.List;

public interface UserGenerator {
    List<User> generate(int count);
    String getModelName();
}
