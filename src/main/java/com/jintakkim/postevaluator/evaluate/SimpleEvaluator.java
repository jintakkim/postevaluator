package com.jintakkim.postevaluator.evaluate;

import com.jintakkim.postevaluator.core.RecommendAlgorithm;
import com.jintakkim.postevaluator.evaluate.metric.AlgorithmMetric;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SimpleEvaluator implements Evaluator {
    private final List<EvaluatedPost> evaluatedPosts;
    private final AlgorithmMetric algorithmMetric;

    @Override
    public EvaluateResult evaluate(RecommendAlgorithm recommendAlgorithm) {
        List<Double> score = evaluatedPosts.stream().map(EvaluatedPost::score).toList();
        List<Double> predScore = predictScore(recommendAlgorithm);
        double cost = algorithmMetric.calculateCost(score, predScore);
        return new EvaluateResult(algorithmMetric, cost);
    }

    private List<Double> predictScore(RecommendAlgorithm recommendAlgorithm) {
        return evaluatedPosts.stream()
                .map(post -> recommendAlgorithm.calculateScore(post.feature()))
                .toList();
    }

}
