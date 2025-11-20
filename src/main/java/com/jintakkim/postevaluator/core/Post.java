package com.jintakkim.postevaluator.core;

import java.util.Map;

public record Post(
        Long id,
        Map<String, Object> features
) {
    public <T> T getFeature(String featureName, Class<T> type) {
        return type.cast(features.get(featureName));
    }

    public Object getFeature(String featureName) {
        return features.get(featureName);
    }
}
