package com.jintakkim.postevaluator.evaluation.metric;

import com.jintakkim.postevaluator.evaluation.SamplePrediction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MSEMetric implements AlgorithmMetric {
    private final int topErrorCount;

    public MSEMetric(int topErrorCount) {
        this.topErrorCount = topErrorCount;
    }

    @Override
    public String getName() {
        return "MSE";
    }

    @Override
    public MetricResult calculateCost(List<SamplePrediction> predictions) {
        double sumSquaredError = 0.0;
        int scoreSize = predictions.size();
        List<ErrorRecord> errorRecords = new ArrayList<>(scoreSize);

        for (SamplePrediction prediction : predictions) {
            double error = prediction.labeledScore() - prediction.predictScore();
            double squaredError = error * error;
            errorRecords.add(new ErrorRecord(squaredError, prediction));
            sumSquaredError += squaredError;
        }
        double cost = sumSquaredError / scoreSize;
        List<SamplePrediction> highCostPredictions = calHighCost(errorRecords);
        return new MetricResult(highCostPredictions, cost);
    }

    private List<SamplePrediction> calHighCost(List<ErrorRecord> errorRecords) {
        return errorRecords.stream()
                .sorted(Comparator.comparingDouble(ErrorRecord::squaredError).reversed())
                .limit(topErrorCount)
                .map(ErrorRecord::prediction)
                .toList();
    }

    private record ErrorRecord(double squaredError, SamplePrediction prediction) {}
}
