package com.jintakkim.postevaluator.evaluation;

import com.jintakkim.postevaluator.RecommendAlgorithm;

/**
 * 게시글을 평가하는 평가기
 * thread-safe
 */
public interface Evaluator {
    EvaluateResult evaluate(RecommendAlgorithm recommendAlgorithm);
}
