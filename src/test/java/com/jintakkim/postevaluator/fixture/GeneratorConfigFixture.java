package com.jintakkim.postevaluator.fixture;

import com.jintakkim.postevaluator.config.GeminiConfig;
import com.jintakkim.postevaluator.config.GeneratorConfig;

public class GeneratorConfigFixture {
    public static GeminiConfig geminiConfig = new GeminiConfig();
    public static GeneratorConfig generatorConfig = new GeneratorConfig(
            FeatureConfigFixture.DEFAULT_VIEW_CONFIG,
            FeatureConfigFixture.DEFAULT_LIKE_CONFIG,
            FeatureConfigFixture.DEFAULT_DISLIKE_CONFIG,
            FeatureConfigFixture.DEFAULT_COMMENT_CONFIG,
            FeatureConfigFixture.DEFAULT_CONTENT_CONFIG,
            FeatureConfigFixture.DEFAULT_CREATED_AT_CONFIG,
            geminiConfig
    );
}
