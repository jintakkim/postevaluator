package com.jintakkim.postevaluator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.Schema;
import com.google.genai.types.Type;
import com.jintakkim.postevaluator.feature.FeatureProvider;
import com.jintakkim.postevaluator.labeling.GeminiLabeler;
import com.jintakkim.postevaluator.labeling.Labeler;

import java.util.List;
import java.util.Map;

public class LabelerConfig {

    public final Labeler labeler;

    public LabelerConfig(
            FeatureProvider featureProvider,
            GeminiConfig geminiConfig,
            ObjectMapper objectMapper
    ) {
        this.labeler = new GeminiLabeler(featureProvider, geminiConfig.client, buildGenerateContentConfig(), objectMapper);
    }

    private GenerateContentConfig buildGenerateContentConfig() {
        return GenerateContentConfig.builder()
                .responseMimeType("application/json")
                .responseSchema(arrayOfLabeledPostSchema())
                .build();
    }

    private Schema arrayOfLabeledPostSchema() {
        return Schema.builder()
                .type(Type.Known.ARRAY)
                .items(LabeledPostSchema())
                .build();
    }

    private Schema LabeledPostSchema() {
        return Schema.builder()
                .description("게시글의 평가를 담은 Object")
                .type(Type.Known.OBJECT)
                .properties(Map.of(
                        "score", Schema.builder().type(Type.Known.NUMBER).description("0.0~1.0 사이의 스코어, 클 수록 좋다").build(),
                        "reasoning", Schema.builder().type(Type.Known.STRING).description("스코어의 이유").build()
                ))
                .required(List.of("score", "reasoning"))
                .build();
    }
}
