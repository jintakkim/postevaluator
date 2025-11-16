package com.jintakkim.postevaluator.search;

import com.jintakkim.postevaluator.evaluation.EvaluateResult;
import com.jintakkim.postevaluator.search.param.Combination;

public record SearchResult(
        Combination combination,
        EvaluateResult evaluateResult
) {
}
