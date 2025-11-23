package com.jintakkim.postevaluator.gemini;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

record PostResponseDto (
        @JsonProperty(value = "feature", required = true) Map<String, Object> feature
) {
}
