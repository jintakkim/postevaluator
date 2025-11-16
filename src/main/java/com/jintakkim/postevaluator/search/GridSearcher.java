package com.jintakkim.postevaluator.search;

import com.jintakkim.postevaluator.evaluation.EvaluateResult;
import com.jintakkim.postevaluator.evaluation.Evaluator;
import com.jintakkim.postevaluator.search.param.CombinationProvider;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;

@RequiredArgsConstructor
public class GridSearcher {
    private final Evaluator evaluator;
    private final CombinationProvider combinationProvider;

    public SearchResult search(ParametricRecommendAlgorithm parametricRecommendAlgorithm) {
        return combinationProvider.get().stream()
                .map(combination -> {
                    EvaluateResult evaluateResult = evaluator.evaluate(postFeature -> parametricRecommendAlgorithm.calculateScore(postFeature, combination));
                    return new SearchResult(combination, evaluateResult);
                })
                .min(Comparator.comparingDouble((searchResult) -> searchResult.evaluateResult().cost()))
                .orElseThrow(() -> new IllegalStateException("No combinations were evaluated."));
    }

}
