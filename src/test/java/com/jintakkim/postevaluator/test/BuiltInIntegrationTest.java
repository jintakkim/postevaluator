package com.jintakkim.postevaluator.test;

import com.jintakkim.postevaluator.client.PostRecommendTest;
import com.jintakkim.postevaluator.config.ApplicationConfig;
import com.jintakkim.postevaluator.config.properties.*;
import com.jintakkim.postevaluator.persistance.LocalFileDbIntegrationTest;
import com.jintakkim.postevaluator.persistance.SetupStrategy;
import com.jintakkim.postevaluator.search.space.StepIncreaseSearchSpace;

import java.util.Map;

public class BuiltInIntegrationTest extends LocalFileDbIntegrationTest {
    static ApplicationConfig applicationConfig = new ApplicationConfig(
            new GeminiProperties(null), //env를 통해 가져옴
            new DefinitionProperties(
                    DefinitionProperties.BuiltIn.USER_DEFINITION.ALL_BUILTIN_FEATURES_APPLIED,
                    DefinitionProperties.BuiltIn.POST_DEFINITION.ALL_BUILTIN_FEATURES_APPLIED
            ),
            new DatasetProperties(5, 5, SetupStrategy.CLEAR),
            new AlgorithmMetricProperties(AlgorithmMetricProperties.BuiltIn.MEAN_SQUARED_ERROR),
            new SearchProperties(5, Map.of(
                    "likeWeight", new StepIncreaseSearchSpace(0.5, 0.1),
                    "dislikeWeight", new StepIncreaseSearchSpace(0.5, -0.1),
                    "viewWeight", new StepIncreaseSearchSpace(0.5, -0.1)
            ))
    );
    protected PostRecommendTest postRecommendTest = new PostRecommendTest(applicationConfig);
}
