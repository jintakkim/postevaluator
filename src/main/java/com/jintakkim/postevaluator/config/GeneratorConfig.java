package com.jintakkim.postevaluator.config;

import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.Schema;
import com.google.genai.types.Type;
import com.jintakkim.postevaluator.config.feature.*;
import com.jintakkim.postevaluator.generation.FeatureGenerator;
import com.jintakkim.postevaluator.generation.GeminiFeatureGenerator;

import java.util.List;
import java.util.Map;

public class GeneratorConfig {
    public final FeatureGenerator featureGenerator;
    private final FeatureConfig featureConfig;
    private final GeminiConfig geminiConfig;


    public GeneratorConfig(
            FeatureConfig featureConfig,
            GeminiConfig geminiConfig
    ) {
        this.featureConfig = featureConfig;
        this.geminiConfig = geminiConfig;
        featureGenerator = new GeminiFeatureGenerator(geminiConfig.client, buildGenerateContentConfig());
    }

    private GenerateContentConfig buildGenerateContentConfig() {
        return GenerateContentConfig.builder()
                .responseMimeType("application/json")
                .responseSchema(labeledPostSchema())
                .build();
    }

    private Schema labeledPostSchema() {
        return Schema.builder()
                .description("게시글의 feature")
                .type(Type.Known.ARRAY)
                .items(featureSchema())
                .build();
    }

    private Schema featureSchema() {
        return Schema.builder()
                .description("게시글의 feature를 담은 Object")
                .type(Type.Known.OBJECT)
                .properties(Map.of(
                        "viewCount", Schema.builder().type(Type.Known.INTEGER).description(featureConfig.viewCountConfig.getFeatureDescription()).build(),
                        "likeCount", Schema.builder().type(Type.Known.INTEGER).description(featureConfig.likeCountConfig.getFeatureDescription()).build(),
                        "dislikeCount", Schema.builder().type(Type.Known.INTEGER).description(featureConfig.dislikeCountConfig.getFeatureDescription()).build(),
                        "commentCount", Schema.builder().type(Type.Known.INTEGER).description(featureConfig.commentCountConfig.getFeatureDescription()).build(),
                        "content", Schema.builder().type(Type.Known.STRING).description(featureConfig.contentConfig.getFeatureDescription()).build(),
                        "createdAt", Schema.builder().type(Type.Known.STRING).description(featureConfig.createdAtConfig.getFeatureDescription()).build()
                ))
                .required(List.of("viewCount", "likeCount", "dislikeCount", "commentCount", "content", "createdAt"))
                .build();
    }
}
