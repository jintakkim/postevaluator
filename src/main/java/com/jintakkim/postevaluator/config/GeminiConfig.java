package com.jintakkim.postevaluator.config;

import com.google.genai.Client;
import com.jintakkim.postevaluator.config.properties.GeminiProperties;

public class GeminiConfig {
    public final Client client;

    public GeminiConfig(GeminiProperties geminiProperties) {
        String apiKey;
        if(geminiProperties.apiKey() == null) {
            apiKey = System.getenv("GOOGLE_API_KEY");
            if(apiKey == null) throw new RuntimeException("GOOGLE_API_KEY 환경 변수 설정이 되어있지 않습니다.");
        } else {
            apiKey = geminiProperties.apiKey();
        }
        this.client = Client.builder().apiKey(apiKey).build();
    }
}

