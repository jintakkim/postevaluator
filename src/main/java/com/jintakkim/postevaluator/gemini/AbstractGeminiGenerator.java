package com.jintakkim.postevaluator.gemini;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.jintakkim.postevaluator.BatchCallback;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
abstract class AbstractGeminiGenerator<T> {
    protected static final String MODEL_NAME = "gemini-2.5-flash";
    protected static final String CONTENT_TEMPLATE = "스키마를 반영해서 게시글 추천 테스트에 쓸 %s 데이터 셋 %d개를 만들어줘";

    protected final Client client;
    protected final String entityName;
    private final GenerateContentConfig generateContentConfig;

    public AbstractGeminiGenerator(Client client, String entityName, GenerateContentConfig generateContentConfig) {
        this.client = client;
        this.entityName = entityName;
        this.generateContentConfig = generateContentConfig;

    }

    public void generate(int size, BatchCallback<T> callback) {
        if (size <= 0) throw new IllegalArgumentException("생성할려는 특징 데이터셋 사이즈는 0보다 커야합니다.");
        log.debug("Gemini API 호출 ({}개 요청)", size);
        GenerateContentResponse response = client.models.generateContent(
                MODEL_NAME,
                createContent(size),
                generateContentConfig
        );
        validateResponse(response);
        callback.call(parseResponse(response));
    }

    protected String createContent(int size) {
        return String.format(CONTENT_TEMPLATE, entityName , size);
    }

    protected void validateResponse(GenerateContentResponse response) {
        if (response.text() == null) throw new IllegalStateException("Gemini 응답이 비어있습니다.");
    }

    protected abstract List<T> parseResponse(GenerateContentResponse response);

    public String getModelName() {
        return MODEL_NAME;
    }
}
