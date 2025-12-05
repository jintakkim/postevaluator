package com.jintakkim.postevaluator.config.properties;

import com.jintakkim.postevaluator.FeatureType;
import com.jintakkim.postevaluator.PostDefinition;
import com.jintakkim.postevaluator.UserDefinition;
import com.jintakkim.postevaluator.feature.LabelingCriteria;
import com.jintakkim.postevaluator.feature.NumberTypeGenerationCriteriaBuilder;
import com.jintakkim.postevaluator.feature.FeatureDefinition;
import com.jintakkim.postevaluator.feature.GenerationCriteriaBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

public record DefinitionProperties(UserDefinition userDefinition, PostDefinition postDefinition) {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class BuiltIn {
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class USER_FEATURE {
            public static final FeatureDefinition LIKE_TOPICS = new FeatureDefinition(
                    "likeTopics",
                    FeatureType.STRING,
                    new GenerationCriteriaBuilder()
                            .addCondition("유저가 좋아하는 주제들", "(, )로 이어진 간단한 명사 형태")
                            .build()
            );
            public static final FeatureDefinition DISLIKE_TOPICS = new FeatureDefinition(
                    "dislikeTopics",
                    FeatureType.STRING,
                    new GenerationCriteriaBuilder()
                            .addCondition("유저가 싫어하는 주제들", "(, )로 이어진 간단한 명사 형태")
                            .build()
            );
            public static final FeatureDefinition PREFERRED_LENGTH = new FeatureDefinition(
                    "preferredLength",
                    FeatureType.INTEGER,
                    new GenerationCriteriaBuilder()
                            .addCondition("유저가 선호 하는 글의 길이", "길이 별로 숏폼, 중문, 장문에 따라서 1,2,3으로 배정")
                            .build()
            );
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class USER_DEFINITION {
            public static final UserDefinition ALL_BUILTIN_FEATURES_APPLIED = new UserDefinition(
                    Map.of(
                            USER_FEATURE.LIKE_TOPICS.name(), USER_FEATURE.LIKE_TOPICS,
                            USER_FEATURE.DISLIKE_TOPICS.name(), USER_FEATURE.DISLIKE_TOPICS,
                            USER_FEATURE.PREFERRED_LENGTH.name(), USER_FEATURE.PREFERRED_LENGTH
                    ),
                    LabelingCriteria.builder()
                            .addCriterion("좋아하는 주제를 고려하여 점수 부여")
                            .addCriterion("싫어하는 주제를 고려하여 점수 부여")
                            .addCriterion("선호하는 문장 길이를 고려하여 점수 부여")
                            .build()
            );
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class POST_FEATURE {
            public static final FeatureDefinition AUTHOR_REPUTATION = new FeatureDefinition(
                    "reputation",
                    FeatureType.DOUBLE,
                    new NumberTypeGenerationCriteriaBuilder.MinMaxBuilder()
                            .min(0.0)
                            .max(1.0)
                            .addCondition("작성자의 명성을 나타내는 스코어", "클 수록 명성이 높게 생성")
                            .build()
            );
            /**
             * 좋아요 수
             */
            public static final FeatureDefinition LIKE_COUNT = new FeatureDefinition(
                    "likeCount",
                    FeatureType.INTEGER,
                    NumberTypeGenerationCriteriaBuilder.logNormalDistributionBuilder()
                            .median(20L)
                            .std(10.0)
                            .addCondition("최소크기", "0보다 커야한다.")
                            .build()
            );
            /**
             * 싫어요 수
             */
            public static final FeatureDefinition DISLIKE_COUNT = new FeatureDefinition(
                    "dislikeCount",
                    FeatureType.INTEGER,
                    NumberTypeGenerationCriteriaBuilder.logNormalDistributionBuilder()
                            .median(10L)
                            .std(5.0)
                            .addCondition("최소크기", "0보다 커야한다.")
                            .build()
            );
            /**
             * 조회 수
             */
            public static final FeatureDefinition VIEW_COUNT = new FeatureDefinition(
                    "viewCount",
                    FeatureType.INTEGER,
                    NumberTypeGenerationCriteriaBuilder.logNormalDistributionBuilder()
                            .median(1000L)
                            .std(500.0)
                            .addCondition("최소크기", "0보다 커야한다.")
                            .build()
            );
            /**
             * 댓글 수
             */
            public static final FeatureDefinition COMMENT_COUNT = new FeatureDefinition(
                    "commentCount",
                    FeatureType.INTEGER,
                    NumberTypeGenerationCriteriaBuilder.logNormalDistributionBuilder()
                            .median(100L)
                            .std(50.0)
                            .addCondition("최소크기", "0보다 커야한다.")
                            .build()
            );
            /**
             * 내용 요약 토픽들
             */
            public static final FeatureDefinition CONTENT_TOPICS = new FeatureDefinition(
                    "contentTopics",
                    FeatureType.STRING,
                    new GenerationCriteriaBuilder()
                            .addCondition("컨텐츠의 주제", "(, )로 이어진 간단한 명사 형태이다. 최대 3개를 넘지않는다.")
                            .build()
            );
            /**
             * 내용
             */
            public static final FeatureDefinition CONTENT = new FeatureDefinition(
                    "content",
                    FeatureType.STRING,
                    new GenerationCriteriaBuilder()
                            .addCondition("게시물 내용", "200자를 넘지않는다.")
                            .build()
            );
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class POST_DEFINITION {
            public static final PostDefinition ALL_BUILTIN_FEATURES_APPLIED = new PostDefinition(
                    Map.of(
                            POST_FEATURE.AUTHOR_REPUTATION.name(), POST_FEATURE.AUTHOR_REPUTATION,
                            POST_FEATURE.CONTENT_TOPICS.name(), POST_FEATURE.CONTENT_TOPICS,
                            POST_FEATURE.DISLIKE_COUNT.name(), POST_FEATURE.DISLIKE_COUNT,
                            POST_FEATURE.LIKE_COUNT.name(), POST_FEATURE.LIKE_COUNT,
                            POST_FEATURE.CONTENT.name(), POST_FEATURE.CONTENT,
                            POST_FEATURE.VIEW_COUNT.name(), POST_FEATURE.VIEW_COUNT
                    ),
                    LabelingCriteria.builder()
                            .addCriterion("유저 명성도가 클 수록 점수 가중치를 부여한다.")
                            .addCriterion("조회수, 싫어요수, 좋아요수는 조회수에 비례하여 커지기 떄문에 이를 고려하여 점수를 부여한다.")
                            .build()
            );
        }
    }
}
