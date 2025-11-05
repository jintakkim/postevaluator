package com.jintakkim.postevaluator.evaluator;

import com.jintakkim.postevaluator.core.RecommendAlgorithm;

/**
 * 게시글을 평가하는 평가기
 */
public interface Evaluator {
    EvaluateResult evaluate(RecommendAlgorithm recommendAlgorithm);
}
