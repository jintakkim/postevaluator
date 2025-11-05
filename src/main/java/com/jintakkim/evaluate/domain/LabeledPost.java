package com.jintakkim.evaluate.domain;

public record LabeledPost(
        PostFeature feature,
        double score,
        //optional
        String reasoning
) {
}
