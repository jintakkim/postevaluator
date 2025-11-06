package com.jintakkim.postevaluator.core;

public record PostFeature(
        long id,
        long viewCount,
        long likeCount,
        long dislikeCount,
        long commentCount,
        String content,
        String createdAt
) {
}
