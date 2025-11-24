package com.jintakkim.postevaluator.feature;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateTypeGenerationCriteriaBuilder extends GenerationCriteriaBuilder {
    private static final String DATE_TYPE = "ISO-8601";
    private Integer maxAge;

    public DateTypeGenerationCriteriaBuilder() {
        super();
    }

    public DateTypeGenerationCriteriaBuilder maxAge(int maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    @Override
    public GenerationCriteria build() {
        if(maxAge != null) {
            attributes.put("maxAge", String.valueOf(maxAge));
        }
        attributes.put("dateType", DATE_TYPE);
        return super.build();
    }

}
