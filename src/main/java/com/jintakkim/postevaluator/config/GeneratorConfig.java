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
    private final ViewCountConfig viewCountConfig;
    private final LikeCountConfig likeCountConfig;
    private final DislikeCountConfig dislikeCountConfig;
    private final CommentCountConfig commentCountConfig;
    private final ContentConfig contentConfig;
    private final CreatedAtConfig createdAtConfig;
    private final GeminiConfig geminiConfig;


    public GeneratorConfig(
            ViewCountConfig viewCountConfig,
            LikeCountConfig likeCountConfig,
            DislikeCountConfig dislikeCountConfig,
            CommentCountConfig commentCountConfig,
            ContentConfig contentConfig,
            CreatedAtConfig createdAtConfig,
            GeminiConfig geminiConfig
    ) {
        this.viewCountConfig = viewCountConfig;
        this.likeCountConfig = likeCountConfig;
        this.dislikeCountConfig = dislikeCountConfig;
        this.commentCountConfig = commentCountConfig;
        this.contentConfig = contentConfig;
        this.createdAtConfig = createdAtConfig;
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
                        "viewCount", Schema.builder().type(Type.Known.INTEGER).description(viewCountConfig.getCriteria()).build(),
                        "likeCount", Schema.builder().type(Type.Known.INTEGER).description(likeCountConfig.getCriteria()).build(),
                        "dislikeCount", Schema.builder().type(Type.Known.INTEGER).description(dislikeCountConfig.getCriteria()).build(),
                        "commentCount", Schema.builder().type(Type.Known.INTEGER).description(commentCountConfig.getCriteria()).build(),
                        "content", Schema.builder().type(Type.Known.STRING).description(contentConfig.getCriteria()).build(),
                        "createdAt", Schema.builder().type(Type.Known.STRING).description(createdAtConfig.getCriteria()).build()
                ))
                .required(List.of("viewCount", "likeCount", "dislikeCount", "commentCount", "content", "createdAt"))
                .build();
    }
}
