package com.jintakkim.postevaluator;

import java.util.Map;

public record User (
        Long id,
        Map<String, Object> features
) implements DynamicEntity {
    public static final String name = "user";

    public User(Map<String, Object> fields) {
        this(null, fields);
    }

    @Override
    public Object get(String fieldName) {
        return features.get(fieldName);
    }

    public <T> T getFeature(String fieldName, Class<T> type) {
        return type.cast(get(fieldName));
    }
}
