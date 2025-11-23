package com.jintakkim.postevaluator.evaluation.metric;

import com.jintakkim.postevaluator.evaluation.SamplePrediction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MSEMetric implements AlgorithmMetric {
    private static final int DEFAULT_TOP_ERROR_COUNT = 0;
    private final int topErrorCount;

    public MSEMetric(int topErrorCount) {
        this.topErrorCount = topErrorCount;
    }

    public MSEMetric() {
        this(DEFAULT_TOP_ERROR_COUNT);
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
            errorRecords.add(new ErrorRecord(squaredError, prediction.postId()));
            sumSquaredError += squaredError;
        }
        double cost = sumSquaredError / scoreSize;
        List<Long> highCostPostIds = calHighCost(errorRecords);
        return new MetricResult(highCostPostIds, cost);
    }

    private List<Long> calHighCost(List<ErrorRecord> errorRecords) {
        return errorRecords.stream()
                .sorted(Comparator.comparingDouble(ErrorRecord::squaredError).reversed())
                .limit(topErrorCount)
                .map(ErrorRecord::postId)
                .toList();
    }

    private record ErrorRecord(double squaredError, long postId) {}
}
