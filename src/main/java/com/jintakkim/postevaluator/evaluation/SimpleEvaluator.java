package com.jintakkim.postevaluator.evaluation;

import com.jintakkim.postevaluator.core.RecommendAlgorithm;
import com.jintakkim.postevaluator.evaluation.metric.AlgorithmMetric;
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
        double cost = algorithmMetric.calculateCost(score, predScore);
        return new EvaluateResult(algorithmMetric, cost);
    }

    private List<Double> predictScore(RecommendAlgorithm recommendAlgorithm) {
        return evaluationPosts.stream()
                .map(post -> recommendAlgorithm.calculateScore(post.feature()))
                .toList();
    }

}
