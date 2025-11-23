package com.jintakkim.postevaluator.core;

import java.util.List;

public interface PostGenerator {
    List<Post> generate(int size);
    String getModelName();
}
