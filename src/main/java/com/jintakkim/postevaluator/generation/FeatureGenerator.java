package com.jintakkim.postevaluator.generation;

import com.jintakkim.postevaluator.core.Post;

import java.util.List;

public interface FeatureGenerator {
    List<Post> generate(int size);
    String getModelName();
}
