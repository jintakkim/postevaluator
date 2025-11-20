package com.jintakkim.postevaluator.evaluation.metric;

import com.jintakkim.postevaluator.evaluation.PostPrediction;

import java.util.List;

public class RMSEMetric implements AlgorithmMetric {
    private static final int DEFAULT_TOP_ERROR_COUNT = 0;

    private final MSEMetric mseMetric;

    public RMSEMetric(int topErrorCount) {
        this.mseMetric = new MSEMetric(topErrorCount);
    }

    public RMSEMetric() {
        this(DEFAULT_TOP_ERROR_COUNT);
    }

    @Override
    public String getName() {
        return "RMSE";
    }

    @Override
    public MetricResult calculateCost(List<PostPrediction> postPredictions) {
        MetricResult mseMetricResult = mseMetric.calculateCost(postPredictions);
        return new MetricResult(mseMetricResult.topErrorOccurredPostIds(), Math.sqrt(mseMetricResult.cost()));
    }
}
