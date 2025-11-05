package com.jintakkim.postevaluator.evaluate.metric;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MSEMetric implements AlgorithmMetric {
    @Override
    public String getName() {
        return "MSE";
    }

    @Override
    public double calculateCost(List<Double> score, List<Double> scorePredict) {
        validateInput(score, scorePredict);
        double sumSquaredError = 0.0;
        for (int i = 0; i < score.size(); i++) {
            double error = score.get(i) - scorePredict.get(i);
            sumSquaredError += (error * error);
        }
        return sumSquaredError / score.size();
    }

    private void validateInput(List<Double> score, List<Double> scorePredict) {
        if (scorePredict == null || score == null || score.size() != scorePredict.size())
            throw new IllegalArgumentException("score과 scorePredict 리스트는 null이 아니어야 하며, 크기가 같아야 합니다.");
    }
}
