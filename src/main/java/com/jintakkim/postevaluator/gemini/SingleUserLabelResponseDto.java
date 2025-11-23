package com.jintakkim.postevaluator.gemini;

import com.fasterxml.jackson.annotation.JsonProperty;

record SingleUserLabelResponseDto(
        @JsonProperty(required = true, value = "postId") Long postId,
        @JsonProperty(required = true, value = "score") double score,
        @JsonProperty(required = true, value = "reasoning") String reasoning
) {
}
