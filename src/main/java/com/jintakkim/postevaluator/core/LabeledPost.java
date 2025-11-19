package com.jintakkim.postevaluator.core;

public record LabeledPost(
        Long id,
        long postId,
        double score,
        String reasoning
) {

    public LabeledPost {
        validate(score, reasoning);
    }

    public LabeledPost(long postId, double score, String reasoning) {
        this(null, postId, score, reasoning);
    }

    public LabeledPost(long postId, String score, String reasoning) {
        this(null, postId, parseScore(score), reasoning);
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
