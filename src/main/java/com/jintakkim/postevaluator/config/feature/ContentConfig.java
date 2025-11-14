package com.jintakkim.postevaluator.config.feature;

import com.jintakkim.postevaluator.core.FeatureProperty;
import com.jintakkim.postevaluator.core.FeaturePropertyProvider;
import com.jintakkim.postevaluator.core.FeatureType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContentConfig implements FeaturePropertyProvider {
    private static final String DEFAULT_TOPIC = "자유 주제 형식";
    private static final String DEFAULT_CRITERIA = """
            게시글은 사용자에게 좋은 글의 가치를 제공해야 한다
            적절한 길이는 정보의 깊이와 가독성을 모두 반영하는 품질 판단의 요소이다
            """;
    private static final int DEFAULT_MIN_CONTENT_SIZE = 0;
    private static final int MAX_CONTENT_SIZE = 200;
    private final int minContentSize;
    private final int maxContentSize;
    private final String topic;
    private final String criteria;

    private ContentConfig(int maxContentSize, int minContentSize, String topic, String criteria) {
        this.minContentSize = minContentSize;
        this.maxContentSize = maxContentSize;
        this.topic = topic;
        this.criteria = criteria;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCriteria() {
        return criteria;
    }

    @Override
    public FeatureProperty getFeatureProperty() {
        return new FeatureProperty() {
            @Override
            public String getName() {
                return "content";
            }

            @Override
            public String getFeatureDescription() {
                return String.format("내용이다, %d자 ~ %d자 사이의 글이며 주제는 %s이다, 다양한 퀄리티의 글이 존재한다.", maxContentSize, minContentSize, topic);
            }

            @Override
            public FeatureType getFeatureType() {
                return FeatureType.STRING;
            }
        };
    }

    public static class Builder {
        private String criteria;
        private String topic;
        private Integer minContentSize;
        private Integer maxContentSize;

        public ContentConfig.Builder minContentSize(int minContentSize) {
            this.minContentSize = minContentSize;
            return this;
        }

        public ContentConfig.Builder maxContentSize(int maxContentSize) {
            this.maxContentSize = maxContentSize;
            return this;
        }

        public ContentConfig.Builder topic(String topic) {
            this.topic = topic;
            return this;
        }

        public ContentConfig.Builder criteria(String criteria) {
            this.criteria = criteria;
            return this;
        }

        public ContentConfig build() {
            if(topic == null) {
                log.warn("게시글 주제가 설정되지 {}으로 설정합니다", DEFAULT_TOPIC);
                topic = DEFAULT_TOPIC;
            }
            if(criteria == null) {
                criteria = DEFAULT_CRITERIA;
            }
            if(minContentSize == null) {
                minContentSize = DEFAULT_MIN_CONTENT_SIZE;
            }
            if(maxContentSize == null) {
                maxContentSize = MAX_CONTENT_SIZE;
            }
            return new ContentConfig(minContentSize, maxContentSize, topic, criteria);
        }
    }
}
