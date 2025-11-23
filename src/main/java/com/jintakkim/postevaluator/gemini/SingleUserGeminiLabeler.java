package com.jintakkim.postevaluator.gemini;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Schema;
import com.google.genai.types.Type;
import com.jintakkim.postevaluator.*;
import com.jintakkim.postevaluator.labeling.LabelPrompt;
import com.jintakkim.postevaluator.labeling.Labeler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
abstract class SingleUserGeminiLabeler implements Labeler {
    private static final String MODEL_NAME = "gemini-2.0-flash";

    private final UserDefinition userDefinition;
    private final PostDefinition postDefinition;
    private final Client client;
    private final ObjectMapper objectMapper;
    private final GenerateContentConfig generateContentConfig;

    public SingleUserGeminiLabeler(
            UserDefinition userDefinition,
            PostDefinition postDefinition,
            Client client,
            ObjectMapper objectMapper
    ) {
        this.userDefinition = userDefinition;
        this.postDefinition = postDefinition;
        this.client = client;
        this.objectMapper = objectMapper;
        this.generateContentConfig = composeGenerateContentConfig();

    }

    public List<Label> label(User user, List<Post> posts) {
        String content = LabelPrompt.generateContent(
                userDefinition.labelingCriteria(),
                user,
                postDefinition.labelingCriteria(),
                posts
        );
        GenerateContentResponse response = client.models.generateContent(
                MODEL_NAME,
                content,
                generateContentConfig
        );
        List<SingleUserLabelResponseDto> labelDtos = parseResponse(response);
        validateSizeMatch(labelDtos, posts.size());
        return labelDtos.stream().map(labelDto -> convertLabelDtoToLabel(labelDto, user.id())).toList();
    }

    @Override
    public String getModelName() {
        return MODEL_NAME;
    }

    private List<SingleUserLabelResponseDto> parseResponse(GenerateContentResponse response) {
        try {
            log.debug(response.text());
            return objectMapper.readValue(response.text(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("json값 해석중 예외가 발생했습니다.",e);
        }
    }

    private void validateSizeMatch(List<SingleUserLabelResponseDto> response, int size) {
        if(response.size() != size) throw new IllegalStateException("응답의 사이즈가 맞지 않습니다.");
    }

    private Label convertLabelDtoToLabel(SingleUserLabelResponseDto labelDto, long userId) {
        return new Label(labelDto.postId(), userId, labelDto.score(), labelDto.reasoning());
    }

    private GenerateContentConfig composeGenerateContentConfig() {
        return GenerateContentConfig.builder()
                .responseMimeType("application/json")
                .responseSchema(composeLabelsSchema())
                .build();
    }

    private Schema composeLabelsSchema() {
        return Schema.builder()
                .type(Type.Known.ARRAY)
                .items(composeLabelSchema())
                .build();
    }

    private Schema composeLabelSchema() {
        return Schema.builder()
                .description("게시글의 평가를 담은 Object")
                .type(Type.Known.OBJECT)
                .properties(Map.of(
                        "postId", Schema.builder().type(Type.Known.INTEGER).description("게시글 id").build(),
                        "score", Schema.builder().type(Type.Known.NUMBER).description("0.0~1.0 사이의 스코어, 클 수록 좋다").build(),
                        "reasoning", Schema.builder().type(Type.Known.STRING).description("스코어의 이유").build()
                ))
                .required(List.of("postId", "score", "reasoning"))
                .build();
    }
}
