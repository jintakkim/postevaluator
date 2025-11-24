package com.jintakkim.postevaluator.config;

import com.google.genai.Client;

public class GeminiConfig {
    public final Client client;

    public GeminiConfig() {
        validateEnvSetting();
        this.client = new Client();
    }

    private void validateEnvSetting() {
        String apiKey = System.getenv("GOOGLE_API_KEY");
        if(apiKey == null) throw new RuntimeException("GOOGLE_API_KEY 환경 변수 설정이 되어있지 않습니다.");
    }
}

