package com.jintakkim.core;

public record LabeledPost(
        PostFeature feature,
        double score,
        //optional
        String reasoning
) {
}
