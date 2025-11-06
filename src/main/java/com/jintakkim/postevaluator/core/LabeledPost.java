package com.jintakkim.postevaluator.core;

public record LabeledPost(
        long featureId,
        double score,
        String reasoning,
        String labeledModel
) {
}
