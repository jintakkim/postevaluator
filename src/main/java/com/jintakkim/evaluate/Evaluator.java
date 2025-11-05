package com.jintakkim.evaluate;

import com.jintakkim.core.RecommendAlgorithm;

/**
 * 게시글을 평가하는 평가기
 */
public interface Evaluator {
    EvaluateResult evaluate(RecommendAlgorithm recommendAlgorithm);
}
