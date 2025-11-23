package com.jintakkim.postevaluator.core.feature;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DateTypeGenerationCriteriaBuilder {
    private static final String DATE_TYPE = "ISO-8601";

    private Integer maxAge;
    private Map<String, String> customFields = new HashMap<>();

    public DateTypeGenerationCriteriaBuilder maxAge(int maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    public DateTypeGenerationCriteriaBuilder addCustomCondition(String name, String condition) {
        customFields.put(name, condition);
        return this;
    }

    public GenerationCriteria build() {
        Map<String, String> attributes = new HashMap<>(customFields);
        if(maxAge != null) {
            attributes.put("maxAge", String.valueOf(maxAge));
        }
        attributes.put("dateType", DATE_TYPE);
        return new GenerationCriteria(attributes);
    }

}
