package com.jintakkim.postevaluator.test;

import com.google.genai.types.GenerateContentResponse;
import org.mockito.Mockito;

public class MockGeminiContentResponseBuilder {
    private final String text;

    public MockGeminiContentResponseBuilder(String text) {
        this.text = text;
    }

    public GenerateContentResponse build() {
        GenerateContentResponse response = Mockito.mock(GenerateContentResponse.class);
        Mockito.when(response.text()).thenReturn(text);
        return response;
    }
}
