package com.jintakkim.postevaluator.feature;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Map;

@RequiredArgsConstructor
@Getter
@ToString
public class GenerationCriteria {
    private final Map<String, String> attributes;
}
