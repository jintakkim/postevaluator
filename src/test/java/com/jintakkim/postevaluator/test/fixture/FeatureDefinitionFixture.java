package com.jintakkim.postevaluator.test.fixture;

import com.jintakkim.postevaluator.FeatureType;
import com.jintakkim.postevaluator.feature.*;

import java.util.Map;

public final class FeatureDefinitionFixture {
    private FeatureDefinitionFixture() {}

    public static final class Post {
        private Post() {}
        public static final FeatureDefinition COMMENT_COUNT = new FeatureDefinition(
                "commentCount",
                FeatureType.INTEGER,
                NumberTypeGenerationCriteriaBuilder.logNormalDistributionBuilder()
                        .median(10L)
                        .std(0.5)
                        .build()
        );

        public static final FeatureDefinition LIKE_COUNT = new FeatureDefinition(
                "likeCount",
                FeatureType.INTEGER,
                NumberTypeGenerationCriteriaBuilder.logNormalDistributionBuilder()
                        .median(40L)
                        .std(0.5)
                        .build()
        );

        public static final FeatureDefinition CONTENT = new FeatureDefinition(
                "content",
                FeatureType.STRING,
                new GenerationCriteriaBuilder()
                        .addCondition("글자수", "200자 이하")
                        .build()
        );

        public static final FeatureDefinition CREATED_AT = new FeatureDefinition(
                "createdAt",
                FeatureType.DATE,
                new DateTypeGenerationCriteriaBuilder()
                        .maxAge(20)
                        .build()
        );
    }

    public static final class User {
        private User() {}

        public static final FeatureDefinition LIKE_TOPIC = new FeatureDefinition(
                "likeTopic",
                FeatureType.STRING,
                new GenerationCriteria(Map.of())
        );

        public static final FeatureDefinition DISLIKE_TOPIC = new FeatureDefinition(
                "dislikeTopic",
                FeatureType.STRING,
                new GenerationCriteria(Map.of())
        );
    }
}
