package com.jintakkim.postevaluator.generation;

import com.jintakkim.postevaluator.User;

import java.util.List;

public interface UserGenerator {
    List<User> generate(int count);
    String getModelName();
}
