package com.jintakkim.postevaluator.fixture;

import com.jintakkim.postevaluator.config.GeminiConfig;
import com.jintakkim.postevaluator.config.GeneratorConfig;

public class GeneratorConfigFixture {
    public static GeminiConfig geminiConfig = new GeminiConfig();
    public static GeneratorConfig generatorConfig = new GeneratorConfig(
            FeatureConfigFixture.DEFAULT_FEATURE_CONFIG,
            geminiConfig
    );
}
