package com.jintakkim.postevaluator.generation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.jintakkim.postevaluator.core.PostFeature;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class GeminiFeatureGenerator implements FeatureGenerator {
    private static final String MODEL_NAME = "gemini-2.0-flash";
    private static final String CONTENT_TEMPLATE = "스키마를 반영해서 게시글 추천 테스트에 쓸 데이터 셋 %d개를 만들어줘";
    private final Client client;
    private final GenerateContentConfig generateContentConfig;
    private final ObjectMapper objectMapper;

    public GeminiFeatureGenerator(
            Client client,
            GenerateContentConfig generateContentConfig
            ) {
        this.client = client;
        this.generateContentConfig = generateContentConfig;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public List<PostFeature> generate(int size) {
        if (size <= 0) throw new IllegalArgumentException("생성할려는 특징 데이터셋 사이즈는 0보다 커야합니다.");
        log.debug("Gemini API 호출 ({}개 요청)", size);
        GenerateContentResponse response = client.models.generateContent(
                MODEL_NAME,
                String.format(CONTENT_TEMPLATE, size),
                generateContentConfig
        );
        validateResponse(response);
        return parseResponse(response);
    }

    private void validateResponse(GenerateContentResponse response) {
        if (response.text() == null) throw new IllegalStateException("Gemini 응답이 비어있습니다.");
    }

    private List<PostFeature> parseResponse(GenerateContentResponse response) {
        try {
            System.out.println(response.text());
            return objectMapper.readValue(response.text(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("json값 해석중 예외가 발생했습니다.",e);
        }
    }

    @Override
    public String getModelName() {
        return MODEL_NAME;
    }
}
