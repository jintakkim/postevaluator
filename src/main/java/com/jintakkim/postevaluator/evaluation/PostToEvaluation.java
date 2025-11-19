package com.jintakkim.postevaluator.evaluation;

import com.jintakkim.postevaluator.core.Post;

/**
 * 평가 데이터, 라벨링된 데이터이다.
 */
public record PostToEvaluation(
        Post post,
        double labeledScore,
        String labeledReason
) {
}
