package com.jintakkim.postevaluator.core;

import java.util.Map;

public record Post(
        Long id,
        Map<String, Object> features
) {
}
