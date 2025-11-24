package com.jintakkim.postevaluator.gemini;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Schema;
import com.google.genai.types.Type;
import com.jintakkim.postevaluator.User;
import com.jintakkim.postevaluator.UserDefinition;
import com.jintakkim.postevaluator.generation.UserGenerator;
import com.jintakkim.postevaluator.feature.FeatureDefinition;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class GeminiUserGenerator extends AbstractGeminiGenerator<User> implements UserGenerator {
    private final UserDefinition userDefinition;
    private final ObjectMapper objectMapper;

    public GeminiUserGenerator(
            Client client,
            ObjectMapper objectMapper,
            UserDefinition userDefinition
    ) {
        super(client, User.name, composeGenerateContentConfig(userDefinition));
        this.userDefinition = userDefinition;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<User> generate(int count) {
        return List.of();
    }

    @Override
    protected List<User> parseResponse(GenerateContentResponse response) {
        try {
            log.info(response.text());
            List<UserResponseDto> userDtos = objectMapper.readValue(response.text(), new TypeReference<>() {});
            return userDtos.stream().map(this::convertUserDtoToUser).toList();
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("json 값 해석중 예외가 발생했습니다.",e);
        }
    }

    private User convertUserDtoToUser(UserResponseDto userDto) {
        Map<String, Object> features = userDto.feature().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> userDefinition.parseFeature(entry.getKey(), entry.getValue()))
                );
        return new User(features);
    }

    private static GenerateContentConfig composeGenerateContentConfig(UserDefinition definition) {
        return GenerateContentConfig.builder()
                .responseMimeType("application/json")
                .responseSchema(composeSchemaFromDefinition(definition))
                .build();
    }

    private static Schema composeSchemaFromDefinition(UserDefinition definition) {
        return Schema.builder()
                .description(User.name)
                .type(Type.Known.ARRAY)
                .items(composeUserSchema(definition))
                .build();
    }

    private static Schema composeUserSchema(UserDefinition userDefinition) {
        return Schema.builder()
                .type(Type.Known.OBJECT)
                .description("user")
                .properties(Map.of("feature", composeFeatureSchema(userDefinition)))
                .required(List.of("feature"))
                .build();
    }

    private static Schema composeFeatureSchema(UserDefinition definition) {
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
