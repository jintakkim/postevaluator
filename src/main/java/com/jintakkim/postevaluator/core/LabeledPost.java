package com.jintakkim.postevaluator.core;

public record LabeledPost(
        PostFeature feature,
        double score,
        String reasoning
) {
}
