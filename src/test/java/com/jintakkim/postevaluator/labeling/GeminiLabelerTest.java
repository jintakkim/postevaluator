package com.jintakkim.postevaluator.labeling;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.jintakkim.postevaluator.Label;
import com.jintakkim.postevaluator.Post;
import com.jintakkim.postevaluator.UnlabeledSample;
import com.jintakkim.postevaluator.User;
import com.jintakkim.postevaluator.gemini.SequentialGeminiLabeler;
import com.jintakkim.postevaluator.test.MockGeminiClientBuilder;
import com.jintakkim.postevaluator.test.MockGeminiContentResponseBuilder;
import com.jintakkim.postevaluator.test.fixture.DefinitionFixture;
import com.jintakkim.postevaluator.test.fixture.PostFixture;
import com.jintakkim.postevaluator.test.fixture.UserFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class GeminiLabelerTest {
    static final ObjectMapper objectMapper = new ObjectMapper();

    static final User USER = UserFixture.setId(UserFixture.DEFINITION_1.USER_1, 1L);
    static final Post POST_1 = PostFixture.setId(PostFixture.DEFINITION_1.POST_1, 1L);
    static final Post POST_2 = PostFixture.setId(PostFixture.DEFINITION_1.POST_2, 2L);

    @Test
    @DisplayName("스코어가 0.0 ~ 1.0 사이가 아니라면 예외가 발생해야 한다")
    void shouldThrowExceptionWhenLabelScoreIsOutOfRange() {
        var invalidResponse = List.<Map<String, Object>>of(
                Map.of("postId", 1L, "score", 1.2, "reasoning", "invalid score")
        );
        Labeler labeler = createLabelerWithMockResponse(invalidResponse);
        Assertions.assertThatThrownBy(() ->
                labeler.label(List.of(new UnlabeledSample(USER, POST_1)), (_) -> {})
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("reasoning 필드가 비어있거나 누락되면 예외가 발생해야 한다")
    void shouldThrowExceptionWhenLabelReasoningIsMissing() {
        var missingFieldResponse = List.<Map<String, Object>>of(
                Map.of("postId", 1L, "score", 0.5)
        );
        Labeler labeler = createLabelerWithMockResponse(missingFieldResponse);
        Assertions.assertThatThrownBy(() ->
                labeler.label(List.of(new UnlabeledSample(USER, POST_1)), (_) -> {})
        ).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("요청한 샘플 개수와 응답된 데이터 개수가 일치하지 않으면 예외가 발생해야 한다")
    void shouldThrowExceptionWhenResponseCountMismatch() {
        var partialResponse = List.<Map<String, Object>>of(
                Map.of("postId", 1L, "score", 0.2, "reasoning", "valid")
        );
        Labeler labeler = createLabelerWithMockResponse(partialResponse);
        Assertions.assertThatThrownBy(() ->
                labeler.label(List.of(
                        new UnlabeledSample(USER, POST_1),
                        new UnlabeledSample(USER, POST_2)
                ), (_) -> {})
        ).isInstanceOf(IllegalStateException.class);
    }

    private Labeler createLabelerWithMockResponse(List<Map<String, Object>> responseData) {
        String responseText = getResponseTextFromSource(responseData);
        GenerateContentResponse mockResponse = new MockGeminiContentResponseBuilder(responseText).build();
        Client mockClient = new MockGeminiClientBuilder(mockResponse).build();

        return new SequentialGeminiLabeler(
                DefinitionFixture.USER_DEFINITION_1,
                DefinitionFixture.POST_DEFINITION_1,
                mockClient,
                objectMapper
        );
    }

    private static String getResponseTextFromSource(List<Map<String, Object>> source)  {
        try {
            return objectMapper.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
