package com.jintakkim.postevaluator.gemini;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Schema;
import com.google.genai.types.Type;
import com.jintakkim.postevaluator.Post;
import com.jintakkim.postevaluator.PostDefinition;
import com.jintakkim.postevaluator.feature.FeatureDefinition;
import com.jintakkim.postevaluator.generation.PostGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Slf4j
public class GeminiPostGenerator extends AbstractBatchGeminiGenerator<Post> implements PostGenerator {
    private final ObjectMapper objectMapper;
    private final PostDefinition postDefinition;

    public GeminiPostGenerator(
            Client client,
            ObjectMapper objectMapper,
            PostDefinition postDefinition,
            ExecutorService executorService
            ) {
        super(client, Post.name, composeGenerateContentConfig(postDefinition), executorService);
        this.objectMapper = objectMapper;
        this.postDefinition = postDefinition;
    }

    @Override
    protected List<Post> parseResponse(GenerateContentResponse response) {
        try {
            log.info(response.text());
            List<PostResponseDto> postDtos = objectMapper.readValue(response.text(), new TypeReference<>() {});
            return postDtos.stream().map(this::convertPostDtoToPost).toList();
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("json 값 해석중 예외가 발생했습니다.",e);
        }
    }

    private Post convertPostDtoToPost(PostResponseDto postDto) {
            Map<String, Object> features = postDto.feature().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> postDefinition.parseFeature(entry.getKey(), entry.getValue()))
                );
            return new Post(features);
    }

    private static GenerateContentConfig composeGenerateContentConfig(PostDefinition definition) {
        return GenerateContentConfig.builder()
                .responseMimeType("application/json")
                .responseSchema(composeSchemaFromDefinition(definition))
                .build();
    }

    private static Schema composeSchemaFromDefinition(PostDefinition definition) {
        return Schema.builder()
                .description(Post.name)
                .type(Type.Known.ARRAY)
                .items(composePostSchema(definition))
                .build();
    }

    private static Schema composePostSchema(PostDefinition definition) {
        return Schema.builder()
                .type(Type.Known.OBJECT)
                .description("post")
                .properties(Map.of("feature", composeFeatureSchema(definition)))
                .required(List.of("feature"))
                .build();
    }

    private static Schema composeFeatureSchema(PostDefinition definition) {
        Map<String, Schema> schemaProperties = definition.featureDefinitions().values().stream()
                .collect(Collectors.toMap(
                        FeatureDefinition::name,
                        feature -> Schema.builder()
                                .type(GeminiUtils.parseType(feature.type()))
                                .description(feature.generationCriteria().toString())
                                .build()
                ));
        List<String> requiredFields = new ArrayList<>(definition.featureDefinitions().keySet());
        return Schema.builder()
                .type(Type.Known.OBJECT)
                .properties(schemaProperties)
                .required(requiredFields)
                .build();
    }
}
