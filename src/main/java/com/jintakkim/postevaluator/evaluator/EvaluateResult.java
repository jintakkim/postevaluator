package com.jintakkim.postevaluator.evaluator;

import com.jintakkim.postevaluator.evaluator.metric.AlgorithmMetric;

public record EvaluateResult(
        // 사용된 평가 지표
        AlgorithmMetric algorithmMetric,
        // 평가 지표를 통해 측정된 비용
        double cost

) {
}
