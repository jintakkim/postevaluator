package com.jintakkim.postevaluator.config.feature;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class CreatedAtConfig {
    private static final int DEFAULT_MAX_AGE = 30;
    private static final String DEFAULT_CRITERIA = """
            생성일은 콘텐츠의 최신성을 나타내는 지표로,
            오래될수록 부정적인 요소 작용한다
            """;

    private final int maxAge;
    private final String criteria;

    public CreatedAtConfig(int maxAge, String criteria) {
        if (maxAge < 0) {
            throw new IllegalArgumentException("가장 오래된 생성일은 0 이상이어야 합니다.");
        }
        this.maxAge = maxAge;
        this.criteria = criteria;
    }

    public static CreatedAtConfig.Builder builder() {
        return new CreatedAtConfig.Builder();
    }

    public String getFeatureDescription() {
        return String.format("ISO-8601형식를 따르는 생성시간이다, 가장 오래된 생성일은 %d이다", maxAge);
    }

    public String getCriteria() {
        return criteria;
    }

    public static class Builder {
        private Integer maxAge;
        private String criteria;

        public CreatedAtConfig.Builder criteria(String criteria) {
            this.criteria = criteria;
            return this;
        }

        public CreatedAtConfig.Builder maxAge(int maxAge) {
            this.maxAge = maxAge;
            return this;
        }

        public CreatedAtConfig build() {
            if(maxAge == null) {
                log.warn("최대 생성일이 설정되지 않아 {}으로 설정합니다", maxAge);
                maxAge = DEFAULT_MAX_AGE;
            }
            if(criteria == null) {
                criteria = DEFAULT_CRITERIA;
            }
            return new CreatedAtConfig(maxAge, criteria);
        }
    }
}
