package com.jintakkim.postevaluator.core.feature;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ContentTypeGenerationCriteriaBuilder {
    private static final String DEFAULT_TOPIC = "자유 주제";

    private String topic;
    private Map<String, String> customFields = new HashMap<>();

    public ContentTypeGenerationCriteriaBuilder topic(String topic) {
        this.topic = topic;
        return this;
    }

    public ContentTypeGenerationCriteriaBuilder addCustomCondition(String name, String condition) {
        customFields.put(name, condition);
        return this;
    }

    public GenerationCriteria build() {
        if(topic == null) {
            log.info("주제가 입력되지 않았습니다, {}로 설정합니다.", DEFAULT_TOPIC);
            topic = DEFAULT_TOPIC;
        }
        Map<String, String> attributes = new HashMap<>(customFields);
        attributes.put("topic", topic);
        return new GenerationCriteria(attributes);
    }
}
