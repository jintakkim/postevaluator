package com.jintakkim.postevaluator.core;

import java.time.LocalDateTime;

public record PostFeature(
        long viewCount,
        long likeCount,
        long dislikeCount,
        long commentCount,
        String content,
        LocalDateTime createdAt
) {
}
