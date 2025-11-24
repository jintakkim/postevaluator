package com.jintakkim.postevaluator.test;

import com.google.genai.Client;
import com.google.genai.Models;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.*;

public class MockGeminiClientBuilder {
    private final GenerateContentResponse contentResponseToReturn;
    private String expectedModelName;
    private String expectedContent;
    private GenerateContentConfig expectedGenerateContentConfig;

    public MockGeminiClientBuilder(GenerateContentResponse contentResponseToReturn) {
        this.contentResponseToReturn = contentResponseToReturn;
    }

    public MockGeminiClientBuilder expectedModelName(String expectedModelName) {
        this.expectedModelName = expectedModelName;
        return this;
    }

    public MockGeminiClientBuilder expectedContent(String expectedContent) {
        this.expectedContent = expectedContent;
        return this;
    }

    public MockGeminiClientBuilder expectedGenerateContentConfig(GenerateContentConfig expectedGenerateContentConfig){
        this.expectedGenerateContentConfig = expectedGenerateContentConfig;
        return this;
    }

    public Client build()  {
        Client client = Mockito.mock(Client.class);
        Models models = Mockito.mock(Models.class);
        setModel(client, models);
        Mockito.when(models.generateContent(
                    expectedModelName == null ? anyString() : eq(expectedModelName),
                    expectedContent == null ? anyString() : eq(expectedContent),
                    expectedGenerateContentConfig == null ? any() : eq(expectedGenerateContentConfig))
                ).thenReturn(contentResponseToReturn);
        return client;
    }

    private static void setModel(Client client, Models models) {
        TestRefectionUtils.setField(Client.class, client, "models", models);
    }
}
