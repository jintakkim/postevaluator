package com.jintakkim.postevaluator.core;

public record LabeledPost(
        long id,
        long featureId,
        double score,
        String reasoning,
        //래이블링 과정에 사용된 모델
        String model
) {
}
