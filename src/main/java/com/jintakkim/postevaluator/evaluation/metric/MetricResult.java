package com.jintakkim.postevaluator.evaluation.metric;

import com.jintakkim.postevaluator.evaluation.SamplePrediction;

import java.util.List;

public record MetricResult(
        List<SamplePrediction> topErrorOccurred,
        double cost
) {
}
