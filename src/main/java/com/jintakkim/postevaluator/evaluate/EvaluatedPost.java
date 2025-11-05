package com.jintakkim.postevaluator.evaluate;

import com.jintakkim.postevaluator.core.PostFeature;

/**
 * 평가 데이터, 라벨링된 데이터이다.
 */
public record EvaluatedPost(
        PostFeature feature,
        double score
) {
}
