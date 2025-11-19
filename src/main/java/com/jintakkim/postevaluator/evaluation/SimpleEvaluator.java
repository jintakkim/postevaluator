package com.jintakkim.postevaluator.evaluation;

import com.jintakkim.postevaluator.core.RecommendAlgorithm;
import com.jintakkim.postevaluator.evaluation.metric.AlgorithmMetric;
import com.jintakkim.postevaluator.evaluation.metric.MetricResult;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SimpleEvaluator implements Evaluator {
    private final List<PostToEvaluation> postToEvaluations;
    private final AlgorithmMetric algorithmMetric;

    @Override
    public EvaluateResult evaluate(RecommendAlgorithm recommendAlgorithm) {
        List<Double> labelScores = postToEvaluations.stream().map(PostToEvaluation::labeledScore).toList();
        List<Double> predScores = predictScores(recommendAlgorithm);
        MetricResult metricResult = algorithmMetric.calculateCost(labelScores, predScores);
        List<Long> topErrorFeatureIds = convertTopErrorIndexesToFeatureIds(metricResult.topErrorOccurredIndexes());
        return new EvaluateResult(algorithmMetric,metricResult.cost(), topErrorFeatureIds);
    }

    private List<Double> predictScores(RecommendAlgorithm recommendAlgorithm) {
        return postToEvaluations.stream()
                .map(post -> recommendAlgorithm.calculateScore(post.post()))
                .toList();
    }

    private List<Long> convertTopErrorIndexesToFeatureIds(List<Integer> topErrorIndexes) {
        return topErrorIndexes.stream().map(idx -> postToEvaluations.get(idx).post().id()).toList();
    }

}
