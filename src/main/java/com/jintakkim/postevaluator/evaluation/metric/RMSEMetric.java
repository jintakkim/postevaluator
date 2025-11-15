package com.jintakkim.postevaluator.evaluation.metric;

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
    public MetricResult calculateCost(List<Double> score, List<Double> scorePredict) {
        MetricResult mseMetricResult = mseMetric.calculateCost(score, scorePredict);
        return new MetricResult(mseMetricResult.topErrorOccurredIndexes(), Math.sqrt(mseMetricResult.cost()));
    }
}
