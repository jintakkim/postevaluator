package com.jintakkim.postevaluator.config;

import com.google.genai.Client;
import com.google.genai.types.Type;
import com.jintakkim.postevaluator.core.FeatureType;

public class GeminiConfig {
    public final Client client;

    public GeminiConfig() {
        this.client = new Client();
    }

    public Type.Known parseType(FeatureType featureType) {
        return switch (featureType) {
            case INTEGER -> Type.Known.INTEGER;
            case STRING -> Type.Known.STRING;
            case DOUBLE -> Type.Known.NUMBER;
        };
    }
}

