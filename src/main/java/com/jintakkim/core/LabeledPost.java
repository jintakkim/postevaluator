package com.jintakkim.core;

public record LabeledPost(
        PostFeature feature,
        double score,
        String reasoning
) {
}
