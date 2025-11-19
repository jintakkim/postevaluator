package com.jintakkim.postevaluator.fixture;

import com.jintakkim.postevaluator.core.FeatureType;
import com.jintakkim.postevaluator.feature.*;

import java.util.List;

public final class FeatureFixture {
    private FeatureFixture() {}

    public static final Feature COMMENT_COUNT = new Feature(
            "commentCount",
            FeatureType.INTEGER,
            CountTypeGenerationCriteriaBuilder.logNormalDistributionBuilder()
                    .median(10L)
                    .std(0.5)
                    .build(),
            LabelingCriteria.builder()
                    .addCriterion("클 수록 사용자의 관심도와 콘텐츠에 대한 참여도가 높은 것이기 떄문에 높게 평가")
                    .build()
    );

    public static final Feature LIKE_COUNT = new Feature(
            "likeCount",
            FeatureType.INTEGER,
            CountTypeGenerationCriteriaBuilder.logNormalDistributionBuilder()
                    .median(40L)
                    .std(0.5)
                    .build(),
            LabelingCriteria.builder()
                    .addCriterion("클 수록 사용자의 관심도와 콘텐츠에 대한 참여도가 높은 것이기 떄문에 높게 평가")
                    .build()
    );

    public static final Feature DISLIKE_COUNT = new Feature(
            "dislikeCount",
            FeatureType.INTEGER,
            CountTypeGenerationCriteriaBuilder.logNormalDistributionBuilder()
                    .median(10L)
                    .std(0.5)
                    .build(),
            LabelingCriteria.builder()
                    .addCriterion("클 수록 콘텐츠에 대한 사용자의 부정적인 반응이나 불만족이 커진다")
                    .build()
    );

    public static final Feature VIEW_COUNT = new Feature(
            "viewCount",
            FeatureType.INTEGER,
            CountTypeGenerationCriteriaBuilder.logNormalDistributionBuilder()
                    .median(100L)
                    .std(0.5)
                    .build(),
            LabelingCriteria.builder()
                    .addCriterion("클 수록 사용자의 관심도와 콘텐츠의 인기가 커진다.")
                    .build()
    );

    public static final Feature CONTENT = new Feature(
            "content",
            FeatureType.STRING,
            new ContentTypeGenerationCriteriaBuilder()
                    .topic("수능")
                    .addCustomCondition("글자수", "200자 이하")
                    .build(),
            LabelingCriteria.builder()
                    .addCriterion("수능과 관련된 내용일 수록 높게 평가")
                    .addCriterion("정보의 질이 떨어지는 내용은 낮게 평가")
                    .build()
    );

    public static final Feature CREATED_AT = new Feature(
            "createdAt",
            FeatureType.DATE,
            new DateTypeGenerationCriteriaBuilder()
                    .maxAge(20)
                    .build(),
            LabelingCriteria.builder()
                    .addCriterion("오래된 내용일수록 낮게 평가")
                    .build()
    );

    public static final FeatureRegistry PRE_DEFINED_FEATURE_REGISTRY;

    static {
        FeatureRegistry featureRegistry = new FeatureRegistry();
        featureRegistry.register(COMMENT_COUNT);
        featureRegistry.register(LIKE_COUNT);
        featureRegistry.register(DISLIKE_COUNT);
        featureRegistry.register(VIEW_COUNT);
        featureRegistry.register(CONTENT);
        featureRegistry.register(CREATED_AT);
        PRE_DEFINED_FEATURE_REGISTRY = featureRegistry;
    }
}
