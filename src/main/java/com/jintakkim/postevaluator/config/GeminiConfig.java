package com.jintakkim.postevaluator.config;

import com.google.genai.Client;

public class GeminiConfig {
    public final Client client;

    public GeminiConfig() {
        this.client = new Client();
    }
}
