package com.jintakkim.postevaluator.generation;

import com.jintakkim.postevaluator.core.PostFeature;

import java.util.List;

public interface FeatureGenerator {
    List<PostFeature> generate(int size);
    String getModelName();
}
