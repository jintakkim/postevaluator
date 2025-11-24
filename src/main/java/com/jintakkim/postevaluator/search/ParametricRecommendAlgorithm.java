package com.jintakkim.postevaluator.search;

import com.jintakkim.postevaluator.SampleAccessor;

public interface ParametricRecommendAlgorithm {
    /**
     * @return predictScore min 0.0 ~ max 1.0 사이의 수 추천도가 높을 수록 커진다.
     */
    double calculateScore(SampleAccessor sampleAccessor, CombinationAccessor combinationAccessor);
}
