package com.jintakkim.postevaluator.gemini;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Schema;
import com.google.genai.types.Type;
import com.jintakkim.postevaluator.core.Post;
import com.jintakkim.postevaluator.core.PostDefinition;
import com.jintakkim.postevaluator.core.feature.FeatureDefinition;
import com.jintakkim.postevaluator.core.PostGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class GeminiPostGenerator extends AbstractGeminiGenerator<Post> implements PostGenerator {
    private final PostDefinition postDefinition;
    private final ObjectMapper objectMapper;

    public GeminiPostGenerator(
            Client client,
            ObjectMapper objectMapper,
            PostDefinition postDefinition
            ) {
        super(client, Post.name);
        this.postDefinition = postDefinition;
        this.objectMapper = objectMapper;
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

    @Override
    protected Schema composeSchemaFromDefinition() {
        return Schema.builder()
                .description(entityName)
                .type(Type.Known.ARRAY)
                .items(composePostSchema())
                .build();
    }

    private Schema composePostSchema() {
        return Schema.builder()
                .type(Type.Known.OBJECT)
                .description("post")
                .properties(Map.of("feature", composeFeatureSchema()))
                .required(List.of("feature"))
                .build();
    }

    private Schema composeFeatureSchema() {
        Map<String, Schema> schemaProperties = postDefinition.featureDefinitions().values().stream()
                .collect(Collectors.toMap(
                        FeatureDefinition::name,
                        feature -> Schema.builder()
                                .type(GeminiUtils.parseType(feature.type()))
                                .description(feature.generationCriteria().toString())
                                .build()
                ));
        List<String> requiredFields = new ArrayList<>(postDefinition.featureDefinitions().keySet());
        return Schema.builder()
                .type(Type.Known.OBJECT)
                .properties(schemaProperties)
                .required(requiredFields)
                .build();
    }
}
