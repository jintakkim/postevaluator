package com.jintakkim.postevaluator.evaluation.metric;

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
    public MetricResult calculateCost(List<Double> score, List<Double> scorePredict) {
        validateInput(score, scorePredict);
        if(score.isEmpty()) return new MetricResult(List.of(), 0.0);
        double sumSquaredError = 0.0;
        int scoreSize = score.size();
        List<ErrorRecord> errorRecords = new ArrayList<>(scoreSize);

        for (int i = 0; i < scoreSize; i++) {
            double error = score.get(i) - scorePredict.get(i);
            double squaredError = error * error;
            errorRecords.add(new ErrorRecord(squaredError, i));
            sumSquaredError += squaredError;
        }
        double cost = sumSquaredError / scoreSize ;
        List<Integer> highCostIndices = calHighCostIndexes(errorRecords);
        return new MetricResult(highCostIndices, cost);
    }

    private void validateInput(List<Double> score, List<Double> scorePredict) {
        if (scorePredict == null || score == null || score.size() != scorePredict.size())
            throw new IllegalArgumentException("score과 scorePredict 리스트는 null이 아니어야 하며, 크기가 같아야 합니다.");
    }

    private List<Integer> calHighCostIndexes(List<ErrorRecord> errorRecords) {
        return errorRecords.stream()
                .sorted(Comparator.comparingDouble(ErrorRecord::squaredError).reversed())
                .limit(topErrorCount)
                .map(ErrorRecord::originalIndex)
                .toList();
    }

    private record ErrorRecord(double squaredError, int originalIndex) {}
}
