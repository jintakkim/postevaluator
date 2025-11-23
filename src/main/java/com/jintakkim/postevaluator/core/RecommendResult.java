package com.jintakkim.postevaluator.core;

import java.util.Map;

public record RecommendResult(
        long userId,
        /**
         * postId - score Map
         */
        Map<Long, Double> scores
) {
    public static final double MAX_SCORE = 1.0;
    public static final double MIN_SCORE = 0.0;

    public RecommendResult(long userId, Map<Long, Double> scores) {
        this.userId = userId;
        this.scores = scores;
        validateScore();
    }

    private void validateScore() {
        scores.values().forEach(score -> {
            if(score < MIN_SCORE || score > MAX_SCORE)
                throw new IllegalArgumentException("스코어는" + MIN_SCORE + "~" + MAX_SCORE + "사이여야 합니다.");
        });
    }
}
