package com.jintakkim.postevaluator.gemini;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Schema;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
abstract class AbstractGeminiGenerator<T> {
    private static final String MODEL_NAME = "gemini-2.0-flash";
    private static final String CONTENT_TEMPLATE = "스키마를 반영해서 게시글 추천 테스트에 쓸 %s 데이터 셋 %d개를 만들어줘";

    private final Client client;
    protected final String entityName;
    private final GenerateContentConfig generateContentConfig;

    public AbstractGeminiGenerator(Client client, String entityName) {
        this.client = client;
        this.entityName = entityName;
        this.generateContentConfig = composeGenerateContentConfig();

    }

    public List<T> generate(int size) {
        if (size <= 0) throw new IllegalArgumentException("생성할려는 특징 데이터셋 사이즈는 0보다 커야합니다.");
        log.debug("Gemini API 호출 ({}개 요청)", size);
        GenerateContentResponse response = client.models.generateContent(
                MODEL_NAME,
                String.format(CONTENT_TEMPLATE, entityName , size),
                generateContentConfig
        );
        validateResponse(response);
        return parseResponse(response);
    }

    private void validateResponse(GenerateContentResponse response) {
        if (response.text() == null) throw new IllegalStateException("Gemini 응답이 비어있습니다.");
    }

    protected abstract List<T> parseResponse(GenerateContentResponse response);

    private GenerateContentConfig composeGenerateContentConfig() {
        return GenerateContentConfig.builder()
                .responseMimeType("application/json")
                .responseSchema(composeSchemaFromDefinition())
                .build();
    }

    protected abstract Schema composeSchemaFromDefinition();

    public String getModelName() {
        return MODEL_NAME;
    }
}
