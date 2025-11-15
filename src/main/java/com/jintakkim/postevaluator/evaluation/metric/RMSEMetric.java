package com.jintakkim.postevaluator.evaluation.metric;

import java.util.List;

public class RMSEMetric implements AlgorithmMetric {
    private final MSEMetric mseMetric = new MSEMetric();

    @Override
    public String getName() {
        return "RMSE";
    }

    @Override
    public double calculateCost(List<Double> score, List<Double> scorePredict) {
        MSEMetric mseMetric = new MSEMetric();
        return Math.sqrt(mseMetric.calculateCost(score, scorePredict));
    }
}
