package com.jintakkim.postevaluator.evaluation;

import com.jintakkim.postevaluator.LabeledSample;
import com.jintakkim.postevaluator.RecommendAlgorithm;
import com.jintakkim.postevaluator.evaluation.metric.AlgorithmMetric;
import com.jintakkim.postevaluator.evaluation.metric.MetricResult;

import java.util.List;

public class SimpleEvaluator implements Evaluator {
    private final AlgorithmMetric algorithmMetric;
    private final List<LabeledSample> labeledSamples;

    public SimpleEvaluator(AlgorithmMetric algorithmMetric, List<LabeledSample> labeledSamples) {
        this.algorithmMetric = algorithmMetric;
        this.labeledSamples = labeledSamples;
        validate();
    }

    @Override
    public EvaluateResult evaluate(RecommendAlgorithm recommendAlgorithm) {
        List<SamplePrediction> predictions = predictScores(recommendAlgorithm);
        MetricResult metricResult = algorithmMetric.calculateCost(predictions);
        return new EvaluateResult(algorithmMetric, metricResult.cost(), metricResult.topErrorOccurredPostIds());
    }

    private List<SamplePrediction> predictScores(RecommendAlgorithm recommendAlgorithm) {
        return labeledSamples.stream()
                .map(sample -> new SamplePrediction(sample, recommendAlgorithm.calculateScore(sample)))
                .toList();
    }

    private void validate() {
        if(labeledSamples == null || labeledSamples.isEmpty()) {
            throw new IllegalArgumentException("평가 데이터는 한개 이상있어야 합니다.");
        }
    }
}
