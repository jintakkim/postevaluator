package com.jintakkim.postevaluator.evaluation.metric;

import com.jintakkim.postevaluator.evaluation.PostPrediction;

import java.util.List;
public interface AlgorithmMetric {
    /**
     * 알고리즘 평가시 사용된 메트릭 이름( 예: MSE)
     */
    String getName();

    /**
     * 비용(cost)을 계산하는 로직
     * @return 비용
     */
    MetricResult calculateCost(List<PostPrediction> postPredictions);
}
