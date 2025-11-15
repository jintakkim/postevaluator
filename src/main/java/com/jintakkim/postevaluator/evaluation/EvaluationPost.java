package com.jintakkim.postevaluator.evaluation;

import com.jintakkim.postevaluator.core.PostFeature;

/**
 * 평가 데이터, 라벨링된 데이터이다.
 */
public record EvaluationPost(
        PostFeature feature,
        double score,
        String reasoning
) {
}
