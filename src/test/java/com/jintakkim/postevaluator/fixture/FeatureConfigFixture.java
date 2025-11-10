package com.jintakkim.postevaluator.fixture;

import com.jintakkim.postevaluator.config.feature.*;

/**
 * 기본값 경고가 발생하지 않게 명시적으로 설정한다.
 * 갯수 관련 표준편차는 중앙값에서 0.5를 곱한값으로 설정하였다.
 */
public class FeatureConfigFixture {
    private static final ViewCountConfig DEFAULT_VIEW_CONFIG = ViewCountConfig.builder()
            .median(5000L)
            .std(5000L * 0.5)
            .build();

    private static final LikeCountConfig DEFAULT_LIKE_CONFIG = LikeCountConfig.builder()
            .median(100L)
            .std(100L * 0.5)
            .build();

    private static final DislikeCountConfig DEFAULT_DISLIKE_CONFIG = DislikeCountConfig.builder()
            .median(20L)
            .std(20L * 0.5)
            .build();

    private static final CommentCountConfig DEFAULT_COMMENT_CONFIG = CommentCountConfig.builder()
            .median(10L)
            .std(10L * 0.5)
            .build();

    private static final ContentConfig DEFAULT_CONTENT_CONFIG = ContentConfig.builder()
            .minContentSize(0)
            .maxContentSize(200)
            .build();

    private static final CreatedAtConfig DEFAULT_CREATED_AT_CONFIG = CreatedAtConfig.builder()
            .maxAge(10)
            .build();

    public static final FeatureConfig DEFAULT_FEATURE_CONFIG = FeatureConfig.builder()
            .viewCountConfig(DEFAULT_VIEW_CONFIG)
            .likeCountConfig(DEFAULT_LIKE_CONFIG)
            .dislikeCountConfig(DEFAULT_DISLIKE_CONFIG)
            .commentCountConfig(DEFAULT_COMMENT_CONFIG)
            .contentConfig(DEFAULT_CONTENT_CONFIG)
            .createdAtConfig(DEFAULT_CREATED_AT_CONFIG)
            .build();
}
