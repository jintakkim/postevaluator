package com.jintakkim.postevaluator.feature;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class GenerationCriteriaBuilder {
    protected Map<String, String> attributes = new HashMap<>();

    public GenerationCriteriaBuilder addCondition(String name, String condition) {
        attributes.put(name, condition);
        return this;
    }

    public GenerationCriteria build() {
        return new GenerationCriteria(attributes);
    }
}
