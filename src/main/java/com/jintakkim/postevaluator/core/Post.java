package com.jintakkim.postevaluator.core;

import java.util.Map;

public record Post(
        Long id,
        Map<String, Object> features
) implements DynamicEntity {
    public static final String name = "post";

    public Post(Map<String, Object> features) {
        this(null, features);
    }

    @Override
    public Object get(String fieldName) {
        return features.get(fieldName);
    }

    public <T> T getFeature(String fieldName, Class<T> type) {
        return type.cast(get(fieldName));
    }
}
