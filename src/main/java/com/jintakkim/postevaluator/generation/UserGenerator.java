package com.jintakkim.postevaluator.generation;

import com.jintakkim.postevaluator.User;
import com.jintakkim.postevaluator.gemini.BatchCallback;

public interface UserGenerator {
    void generate(int count, BatchCallback<User> callback);
    String getModelName();
}
