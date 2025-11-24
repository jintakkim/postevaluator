package com.jintakkim.postevaluator.generation;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.jintakkim.postevaluator.Post;
import com.jintakkim.postevaluator.gemini.GeminiPostGenerator;
import com.jintakkim.postevaluator.test.MockGeminiClientBuilder;
import com.jintakkim.postevaluator.test.MockGeminiContentResponseBuilder;
import com.jintakkim.postevaluator.test.fixture.DefinitionFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GeminiPostGeneratorTest {
    static final ObjectMapper objectMapper = new ObjectMapper();
    static final List<Map<String, Object>> SOURCE_FEATURES = List.of(
            Map.of("commentCount", 100, "content", "Test Content A"),
            Map.of("commentCount", 200, "content", "Test Content B")
    );
    static final int size = SOURCE_FEATURES.size();
    static final GenerateContentResponse contentResponseToReturn = new MockGeminiContentResponseBuilder(getResponseTextFromSource(SOURCE_FEATURES)).build();
    static final Client client = new MockGeminiClientBuilder(contentResponseToReturn)
            .expectedModelName("gemini-2.0-flash")
            .expectedContent(String.format("스키마를 반영해서 게시글 추천 테스트에 쓸 %s 데이터 셋 %d개를 만들어줘", Post.name, size))
            .build();
    static final PostGenerator postGenerator = new GeminiPostGenerator(
            client,
            objectMapper,
            DefinitionFixture.POST_DEFINITION_1,
            null //작은 데이터는 병렬 작동 안함.
    );

    @Test
    @DisplayName("생성할려는 데이터 사이즈 만큼의 응답이 와야한다")
    void shouldGenerateExactSameNumberOfPostsRequested() {
        List<Post> generatedPosts = new ArrayList<>();
        postGenerator.generate(size, generatedPosts::addAll);
        Assertions.assertThat(generatedPosts)
                .hasSize(size)
                .extracting(Post::features)
                .containsExactlyInAnyOrderElementsOf(SOURCE_FEATURES);
    }

    private static String getResponseTextFromSource(List<Map<String, Object>> source)  {
        List<Map<String, Map<String, Object>>> wrappedResponse = source.stream()
                .map(feature -> Map.of("feature", feature))
                .collect(Collectors.toList());
        try {
            return objectMapper.writeValueAsString(wrappedResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
