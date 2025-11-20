package com.jintakkim.postevaluator.evaluation;

import com.jintakkim.postevaluator.core.RecommendAlgorithm;
import com.jintakkim.postevaluator.evaluation.metric.AlgorithmMetric;
import com.jintakkim.postevaluator.evaluation.metric.MetricResult;

import java.util.List;

public class SimpleEvaluator implements Evaluator {
    private final AlgorithmMetric algorithmMetric;
    private final List<LabeledPostToEvaluation> labeledPostsToEvaluation;

    public SimpleEvaluator(AlgorithmMetric algorithmMetric, List<LabeledPostToEvaluation> labeledPostsToEvaluation) {
        this.algorithmMetric = algorithmMetric;
        this.labeledPostsToEvaluation = labeledPostsToEvaluation;
        validate();
    }

    @Override
    public EvaluateResult evaluate(RecommendAlgorithm recommendAlgorithm) {
        List<Double> labelScores = labeledPostsToEvaluation.stream().map(LabeledPostToEvaluation::labeledScore).toList();
        List<Double> predScores = predictScores(recommendAlgorithm);
        MetricResult metricResult = algorithmMetric.calculateCost(labelScores, predScores);
        List<Long> topErrorFeatureIds = convertTopErrorIndexesToFeatureIds(metricResult.topErrorOccurredIndexes());
        return new EvaluateResult(algorithmMetric,metricResult.cost(), topErrorFeatureIds);
    }

    private List<Double> predictScores(RecommendAlgorithm recommendAlgorithm) {
        return labeledPostsToEvaluation.stream()
                .map(labeledPostToEvaluation -> recommendAlgorithm.calculateScore(labeledPostToEvaluation.post()))
                .toList();
    }

    private List<Long> convertTopErrorIndexesToFeatureIds(List<Integer> topErrorIndexes) {
        return topErrorIndexes.stream().map(idx -> labeledPostsToEvaluation.get(idx).post().id()).toList();
    }

    private void validate() {
        if(labeledPostsToEvaluation == null || labeledPostsToEvaluation.isEmpty()) {
            throw new IllegalArgumentException("평가 데이터는 한개 이상있어야 합니다.");
        }
    }
}
