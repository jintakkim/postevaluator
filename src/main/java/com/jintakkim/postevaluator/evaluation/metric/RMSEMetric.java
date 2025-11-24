package com.jintakkim.postevaluator.evaluation.metric;

import com.jintakkim.postevaluator.evaluation.SamplePrediction;

import java.util.List;

public class RMSEMetric implements AlgorithmMetric {
    private final MSEMetric mseMetric;

    public RMSEMetric(int topErrorCount) {
        this.mseMetric = new MSEMetric(topErrorCount);
    }

    @Override
    public String getName() {
        return "RMSE";
    }

    @Override
    public MetricResult calculateCost(List<SamplePrediction> predictions) {
        MetricResult mseMetricResult = mseMetric.calculateCost(predictions);
        return new MetricResult(mseMetricResult.topErrorOccurred(), Math.sqrt(mseMetricResult.cost()));
    }
}
