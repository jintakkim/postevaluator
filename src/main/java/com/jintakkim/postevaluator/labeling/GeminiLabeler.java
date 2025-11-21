package com.jintakkim.postevaluator.labeling;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.jintakkim.postevaluator.core.Label;
import com.jintakkim.postevaluator.core.Post;
import com.jintakkim.postevaluator.feature.FeatureDefinition;
import com.jintakkim.postevaluator.feature.FeatureDefinitionProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GeminiLabeler implements Labeler {
    private static final String MODEL_NAME = "gemini-2.0-flash";
    private static final String CONTENT_TEMPLATE = """
            <지시문>
            JSON 배열 형태의 '게시글' 데이터 리스트이다
            각 게시글을 해당 아래 평가 기준을 고려하여 0.0 ~ 1.0 사이의 스코어를 매기고 이유를 작성하고 출력한다.
            <평가 기준>
            %s
            <데이터 리스트>
            %s
            <출력>
            출력은 스키마를 따른다
            """;

    private final FeatureDefinitionProvider featureDefinitionProvider;
    private final Client client;
    private final GenerateContentConfig generateContentConfig;
    private final ObjectMapper objectMapper;

    public GeminiLabeler(
            FeatureDefinitionProvider featureDefinitionProvider,
            Client client,
            GenerateContentConfig generateContentConfig,
            ObjectMapper objectMapper
    ) {
        this.featureDefinitionProvider = featureDefinitionProvider;
        this.client = client;
        this.generateContentConfig = generateContentConfig;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Label> label(List<Post> unlabeledPosts) {
        String stringifiedDataPrompt = convertToString(unlabeledPosts);
        GenerateContentResponse response = client.models.generateContent(
                MODEL_NAME,
                String.format(CONTENT_TEMPLATE, generateRatingCriteria(), stringifiedDataPrompt),
                generateContentConfig
        );
        List<LabeledResponse> parsedResponse = parseResponse(response);
        validateSizeMatch(parsedResponse, unlabeledPosts.size());
        return combineWithResponse(unlabeledPosts, parsedResponse);
    }

    private String generateRatingCriteria() {
        return featureDefinitionProvider.getFeatureDefinitions().stream()
                .map(FeatureDefinition::getFormattedLabelingCriteria)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    @Override
    public String getModelName() {
        return MODEL_NAME;
    }

    private String convertToString(List<Post> posts) {
        try {
            return objectMapper.writeValueAsString(posts);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("질문 생성중 예외 발생",e);
        }
    }

    private List<LabeledResponse> parseResponse(GenerateContentResponse response) {
        try {
            System.out.println(response.text());
            return objectMapper.readValue(response.text(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("json값 해석중 예외가 발생했습니다.",e);
        }
    }

    private void validateSizeMatch(List<LabeledResponse> parsedResponse, int size) {
        if(parsedResponse.size() != size) throw new IllegalStateException("응답의 사이즈가 맞지 않습니다.");
    }

    private List<Label> combineWithResponse(List<Post> unlabeledPosts, List<LabeledResponse> response) {
        List<Label> labels = new ArrayList<>();
        for (int i = 0; i < unlabeledPosts.size(); i++) {
            Post feature = unlabeledPosts.get(i);
            LabeledResponse label = response.get(i);
            labels.add(new Label(feature.id(), label.score(), label.reasoning()));
        }
        return labels;
    }
}
