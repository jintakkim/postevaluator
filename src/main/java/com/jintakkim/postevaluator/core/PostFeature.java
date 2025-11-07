package com.jintakkim.postevaluator.core;

public record PostFeature(
        Long id,
        long viewCount,
        long likeCount,
        long dislikeCount,
        long commentCount,
        String content,
        String createdAt
) {
    public PostFeature(long viewCount, long likeCount, long dislikeCount, long commentCount, String content, String createdAt) {
        this(null, viewCount, likeCount, dislikeCount, commentCount, content, createdAt);
    }
}
