package com.jintakkim.postevaluator.labeling;

import com.jintakkim.postevaluator.core.PostFeature;

public record PostFeaturePrompt (
        long viewCount,
        long likeCount,
        long dislikeCount,
        long commentCount,
        String content,
        String createdAt
) {
    public PostFeaturePrompt(PostFeature postFeature) {
        this(
                postFeature.viewCount(),
                postFeature.likeCount(),
                postFeature.dislikeCount(),
                postFeature.commentCount(),
                postFeature.content(),
                postFeature.createdAt()
        );
    }
}
