package com.jintakkim.postevaluator.core;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

public record LabeledPost(
        Long id,
        long featureId,
        double score,
        String reasoning
) {

    public LabeledPost {
        validate(score, reasoning);
    }

    public LabeledPost(long featureId, double score, String reasoning) {
        this(null, featureId, score, reasoning);
    }

    public LabeledPost(long featureId, String score, String reasoning) {
        this(null, featureId, parseScore(score), reasoning);
    }

    private void validate(double score, String reasoning) {
        if(score < 0.0 || score > 1.0) {
            throw new IllegalArgumentException("스코어는 0.0 ~ 1.0사이 값이여야 합니다.");
        }
        if(reasoning == null || reasoning.isBlank()) {
            throw new IllegalArgumentException("이유는 null이거나 빈칸일 수 없습니다.");
        }
    }

    private static double parseScore(String score) {
        try {
            return Double.parseDouble(score);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("스코어는 소수값이여야 합니다: '" + score + "'", e);
        }
    }
}
