package com.jintakkim.postevaluator.core;

public record LabeledPost(
        Long id,
        long featureId,
        double score,
        String reasoning,
        //래이블링 과정에 사용된 모델
        String model
) {
    public LabeledPost(long featureId, double score, String reasoning, String model) {
        this(null, featureId, score, reasoning, model);
    }
}
