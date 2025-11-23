package com.jintakkim.postevaluator.test.fixture;

import com.jintakkim.postevaluator.PostDefinition;
import com.jintakkim.postevaluator.UserDefinition;
import com.jintakkim.postevaluator.feature.LabelingCriteria;

import java.util.Map;

public final class DefinitionFixture {
    private DefinitionFixture() {}

    public static UserDefinition USER_DEFINITION_1 = new UserDefinition(
            Map.of(
                    FeatureDefinitionFixture.User.DISLIKE_TOPIC.name(), FeatureDefinitionFixture.User.DISLIKE_TOPIC,
                    FeatureDefinitionFixture.User.LIKE_TOPIC.name(), FeatureDefinitionFixture.User.LIKE_TOPIC
            ),
            LabelingCriteria.builder()
                    .addCriterion("유저의 topic 관심도")
                    .build()
    );

    public static UserDefinition USER_DEFINITION_2 = new UserDefinition(
            Map.of(
                    FeatureDefinitionFixture.User.LIKE_TOPIC.name(), FeatureDefinitionFixture.User.LIKE_TOPIC
            ),
            LabelingCriteria.builder()
                    .addCriterion("유저의 topic 관심도")
                    .build()
    );

    public static PostDefinition POST_DEFINITION_1 = new PostDefinition(
            Map.of(
                    FeatureDefinitionFixture.Post.COMMENT_COUNT.name(), FeatureDefinitionFixture.Post.COMMENT_COUNT,
                    FeatureDefinitionFixture.Post.CONTENT.name(), FeatureDefinitionFixture.Post.CONTENT
            ),
            LabelingCriteria.builder()
                    .addCriterion("활성도(댓글수)")
                    .addCriterion("가독성")
                    .build()
    );
    public static PostDefinition POST_DEFINITION_2 = new PostDefinition(
            Map.of(
                    FeatureDefinitionFixture.Post.LIKE_COUNT.name(), FeatureDefinitionFixture.Post.LIKE_COUNT,
                    FeatureDefinitionFixture.Post.CONTENT.name(), FeatureDefinitionFixture.Post.CONTENT
            ),
            LabelingCriteria.builder()
                    .addCriterion("좋아요수가 많을 수록 긍정적")
                    .addCriterion("가독성")
                    .build()
    );
}
