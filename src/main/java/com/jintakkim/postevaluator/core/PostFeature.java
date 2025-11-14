package com.jintakkim.postevaluator.core;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

public record PostFeature(
        Long id,
        long viewCount,
        long likeCount,
        long dislikeCount,
        long commentCount,
        String content,
        String createdAt
) {
    public PostFeature {
        validate(viewCount, likeCount, dislikeCount, commentCount, content, createdAt);
    }

    public PostFeature(long viewCount, long likeCount, long dislikeCount, long commentCount, String content, String createdAt) {
        this(null, viewCount, likeCount, dislikeCount, commentCount, content, createdAt);
    }

    private void validate(long viewCount, long likeCount, long dislikeCount, long commentCount, String content, String createdAt) {
        if (viewCount < 0 || likeCount < 0 || dislikeCount < 0 || commentCount < 0) {
            throw new IllegalArgumentException("갯수 인자들은 음수가 될 수 없습니다.");
        }
        if(content == null) {
            throw new IllegalArgumentException("내용은 null일 수 없습니다.");
        }
        try {
            OffsetDateTime.parse(createdAt);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("생성일(createdAt)이 유효한 ISO-8601 형식이 아닙니다: " + createdAt, e);
        }
    }
}
