package com.jintakkim.postevaluator.evaluation;

import com.jintakkim.postevaluator.evaluation.metric.AlgorithmMetric;

import java.util.List;

public record EvaluateResult(
        // 사용된 평가 지표
        AlgorithmMetric algorithmMetric,
        // 평가 지표를 통해 측정된 비용
        double cost,
        List<Long> topErrorOccurredFeatureIds
        ) {
}
