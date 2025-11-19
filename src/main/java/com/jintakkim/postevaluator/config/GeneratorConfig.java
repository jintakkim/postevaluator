package com.jintakkim.postevaluator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.Schema;
import com.google.genai.types.Type;
import com.jintakkim.postevaluator.feature.Feature;
import com.jintakkim.postevaluator.feature.FeatureProvider;
import com.jintakkim.postevaluator.generation.FeatureGenerator;
import com.jintakkim.postevaluator.generation.GeminiFeatureGenerator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GeneratorConfig {
    private final FeatureProvider featureProvider;
    private final GeminiConfig geminiConfig;

    public final FeatureGenerator featureGenerator;

    public GeneratorConfig(
            FeatureProvider featureProvider,
            GeminiConfig geminiConfig,
            ObjectMapper objectMapper
    ) {
        this.featureProvider = featureProvider;
        this.geminiConfig = geminiConfig;
        featureGenerator = new GeminiFeatureGenerator(geminiConfig.client, buildGenerateContentConfig(), objectMapper);
    }

    private GenerateContentConfig buildGenerateContentConfig() {
        return GenerateContentConfig.builder()
                .responseMimeType("application/json")
                .responseSchema(responseSchema())
                .build();
    }

    private Schema responseSchema() {
        return Schema.builder()
                .description("게시글의 post")
                .type(Type.Known.ARRAY)
                .items(featureSchema())
                .build();
    }

    private Schema featureSchema() {
        Map<String, Schema> schemaProperties = featureProvider.getFeatures().stream()
                .collect(Collectors.toMap(
                        Feature::getName,
                        feature -> Schema.builder()
                                .type(geminiConfig.parseType(feature.getType()))
                                .description(feature.getGenerationCriteria().toString())
                                .build()
                ));
        List<String> requiredFields = featureProvider.getFeatureNames();
        return Schema.builder()
                .description("게시글의 post 들을 담은 Object")
                .type(Type.Known.OBJECT)
                .properties(schemaProperties)
                .required(requiredFields)
                .build();
    }
}
