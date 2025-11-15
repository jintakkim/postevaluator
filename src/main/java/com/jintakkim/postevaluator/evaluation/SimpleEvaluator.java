package com.jintakkim.postevaluator.evaluation;

import com.jintakkim.postevaluator.core.RecommendAlgorithm;
import com.jintakkim.postevaluator.evaluation.metric.AlgorithmMetric;
import com.jintakkim.postevaluator.evaluation.metric.MetricResult;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SimpleEvaluator implements Evaluator {
    private final List<EvaluationPost> evaluationPosts;
    private final AlgorithmMetric algorithmMetric;

    @Override
    public EvaluateResult evaluate(RecommendAlgorithm recommendAlgorithm) {
        List<Double> score = evaluationPosts.stream().map(EvaluationPost::score).toList();
        List<Double> predScore = predictScore(recommendAlgorithm);
        MetricResult metricResult = algorithmMetric.calculateCost(score, predScore);
        List<Long> topErrorFeatureIds = convertTopErrorIndicesToFeatureIds(metricResult.topErrorOccurredIndexes());
        return new EvaluateResult(algorithmMetric,metricResult.cost(), topErrorFeatureIds);
    }

    private List<Double> predictScore(RecommendAlgorithm recommendAlgorithm) {
        return evaluationPosts.stream()
                .map(post -> recommendAlgorithm.calculateScore(post.feature()))
                .toList();
    }

    private List<Long> convertTopErrorIndicesToFeatureIds(List<Integer> topErrorIndexes) {
        return topErrorIndexes.stream().map(idx -> evaluationPosts.get(idx).feature().id()).toList();
    }

}
