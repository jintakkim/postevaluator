package com.jintakkim.postevaluator.gemini;

import com.google.genai.types.Type;
import com.jintakkim.postevaluator.FeatureType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class GeminiUtils {
    public static Type.Known parseType(FeatureType featureType) {
        return switch (featureType) {
            case INTEGER -> Type.Known.INTEGER;
            case STRING -> Type.Known.STRING;
            case DOUBLE -> Type.Known.NUMBER;
            case DATE -> Type.Known.STRING;
        };
    }
}
