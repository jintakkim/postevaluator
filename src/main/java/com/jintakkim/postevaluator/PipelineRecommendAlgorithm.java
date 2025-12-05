package com.jintakkim.postevaluator;

public interface PipelineRecommendAlgorithm {
    /**
     * @return predictScore min 0.0 ~ max 1.0 사이의 수 추천도가 높을 수록 커진다.
     */
    double calculateScore(SampleAccessor sampleAccessor, SubAlgorithmResultAccessor subAlgorithmResultAccessor);

    /**
     * 알고리즘간 식별자 역활을 수행한다.
     */
    String name();
}
