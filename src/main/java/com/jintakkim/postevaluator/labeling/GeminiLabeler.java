package com.jintakkim.postevaluator.labeling;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.jintakkim.postevaluator.core.LabeledPost;
import com.jintakkim.postevaluator.core.PostFeature;

import java.util.ArrayList;
import java.util.List;

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

    private final String ratingCriteria;
    private final Client client;
    private final GenerateContentConfig generateContentConfig;
    private final ObjectMapper objectMapper;

    public GeminiLabeler(
            String ratingCriteria,
            Client client,
            GenerateContentConfig generateContentConfig,
            ObjectMapper objectMapper
    ) {
        this.ratingCriteria = ratingCriteria;
        this.client = client;
        this.generateContentConfig = generateContentConfig;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<LabeledPost> label(List<PostFeature> unlabeledFeatures) {
        List<PostFeaturePrompt> dataPrompt = unlabeledFeatures.stream().map(PostFeaturePrompt::new).toList();
        String stringifiedDataPrompt = convertToString(dataPrompt);
        GenerateContentResponse response = client.models.generateContent(
                MODEL_NAME,
                String.format(CONTENT_TEMPLATE, ratingCriteria, stringifiedDataPrompt),
                generateContentConfig
        );
        List<LabeledResponse> parsedResponse = parseResponse(response);
        validateSizeMatch(parsedResponse, unlabeledFeatures.size());
        return combineFeaturesWithResponse(unlabeledFeatures, parsedResponse);
    }

    @Override
    public String getModelName() {
        return MODEL_NAME;
    }

    private String convertToString(List<PostFeaturePrompt> prompts) {
        try {
            return objectMapper.writeValueAsString(prompts);
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

    private List<LabeledPost> combineFeaturesWithResponse(List<PostFeature> unlabeledFeatures, List<LabeledResponse> response) {
        List<LabeledPost> labeledPosts = new ArrayList<>();
        for (int i = 0; i < unlabeledFeatures.size(); i++) {
            PostFeature feature = unlabeledFeatures.get(i);
            LabeledResponse label = response.get(i);
            labeledPosts.add(new LabeledPost(feature.id(), label.score(), label.reasoning()));
        }
        return labeledPosts;
    }
}
