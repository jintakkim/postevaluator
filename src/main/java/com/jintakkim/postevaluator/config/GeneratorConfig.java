package com.jintakkim.postevaluator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.Schema;
import com.google.genai.types.Type;
import com.jintakkim.postevaluator.config.feature.*;
import com.jintakkim.postevaluator.core.FeatureProperty;
import com.jintakkim.postevaluator.core.FeaturePropertyProvider;
import com.jintakkim.postevaluator.generation.FeatureGenerator;
import com.jintakkim.postevaluator.generation.GeminiFeatureGenerator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GeneratorConfig {
    private final FeatureConfig featureConfig;
    private final GeminiConfig geminiConfig;

    public final FeatureGenerator featureGenerator;

    public GeneratorConfig(
            FeatureConfig featureConfig,
            GeminiConfig geminiConfig,
            ObjectMapper objectMapper
    ) {
        this.featureConfig = featureConfig;
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
                .description("게시글의 feature")
                .type(Type.Known.ARRAY)
                .items(featureSchema())
                .build();
    }

    private Schema featureSchema() {
        List<FeatureProperty> featureProperties = featureConfig.getFeatureProperties();
        Map<String, Schema> schemaProperties = featureProperties.stream()
                .collect(Collectors.toMap(
                        FeatureProperty::getName,
                        featureProperty -> Schema.builder()
                                .type(geminiConfig.parseType(featureProperty.getFeatureType()))
                                .description(featureProperty.getFeatureDescription())
                                .build()
                ));
        List<String> requiredFields = featureProperties.stream().map(FeatureProperty::getName).toList();
        return Schema.builder()
                .description("게시글의 feature를 담은 Object")
                .type(Type.Known.OBJECT)
                .properties(schemaProperties)
                .required(requiredFields)
                .build();
    }
}
