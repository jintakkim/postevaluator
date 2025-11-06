package com.jintakkim.postevaluator.core;

public record LabeledPost(
        long id,
        long featureId,
        double score,
        String reasoning,
        String labeledModel
) {
}
