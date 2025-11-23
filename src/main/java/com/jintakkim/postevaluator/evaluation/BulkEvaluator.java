package com.jintakkim.postevaluator.evaluation;

import com.jintakkim.postevaluator.RecommendAlgorithm;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BulkEvaluator {
    private final Evaluator evaluator;

    public List<EvaluateResult> evaluate(List<RecommendAlgorithm> recommendAlgorithms) {
        return recommendAlgorithms.parallelStream().map(evaluator::evaluate).toList();
    }
}
