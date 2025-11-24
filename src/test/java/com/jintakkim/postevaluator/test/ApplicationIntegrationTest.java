package com.jintakkim.postevaluator.test;

import com.jintakkim.postevaluator.client.PostRecommendTest;
import com.jintakkim.postevaluator.config.ApplicationConfig;
import com.jintakkim.postevaluator.config.properties.AlgorithmMetricProperties;
import com.jintakkim.postevaluator.config.properties.DatasetProperties;
import com.jintakkim.postevaluator.config.properties.DefinitionProperties;
import com.jintakkim.postevaluator.persistance.LocalFileDbIntegrationTest;

public class ApplicationIntegrationTest extends LocalFileDbIntegrationTest {
    static ApplicationConfig applicationConfig = new ApplicationConfig(
            new DefinitionProperties(
                    DefinitionProperties.BuiltIn.USER_DEFINITION.ALL_BUILTIN_FEATURES_APPLIED,
                    DefinitionProperties.BuiltIn.POST_DEFINITION.ALL_BUILTIN_FEATURES_APPLIED
            ),
            DatasetProperties.defaults(),
            new AlgorithmMetricProperties(AlgorithmMetricProperties.BuiltIn.MEAN_SQUARED_ERROR)
    );
    protected PostRecommendTest postRecommendTest = new PostRecommendTest(applicationConfig);
}
