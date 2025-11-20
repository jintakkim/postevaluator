package com.jintakkim.postevaluator.evaluation;

public record PostPrediction(
        long postId,
        double labeledScore,
        double predictScore
) {
}
