package com.jintakkim.postevaluator.config.feature;

import com.jintakkim.postevaluator.core.distribution.Distribution;
import com.jintakkim.postevaluator.core.distribution.LogNormalDistribution;

import lombok.extern.slf4j.Slf4j;

/**
 * 조회수 관련 설정 객체
 * 조회수는 로그 정규 분포를 따른다.
 */
@Slf4j
public class ViewCountConfig {
    private static final long DEFAULT_MEDIAN = 10000L;
    private static final double DEFAULT_HEURISTIC_FACTOR = 0.5;
    private static final String DEFAULT_CRITERIA = """
            조회수는 사용자의 관심도와 콘텐츠의 인기를 나타내는 지표.
            """;

    private final Distribution distribution;
    private final String criteria;

    public ViewCountConfig(long median, double std, String criteria) {
        if (median < 0 || std < 0) {
            throw new IllegalArgumentException("중앙값과 표준편차는 0 이상이어야 합니다.");
        }
        this.distribution = new LogNormalDistribution(median, std);
        this.criteria = criteria;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getFeatureDescription() {
        return String.format("조회수이다, 통계적 특성: %s", distribution.getDescription());
    }

    public String getCriteria() {
        return criteria;
    }

    public static class Builder {
        private Long median;
        private Double std;
        private String criteria;

        public Builder criteria(String criteria) {
            this.criteria = criteria;
            return this;
        }

        public Builder median(long median) {
            this.median = median;
            return this;
        }

        public Builder std(double std) {
            this.std = std;
            return this;
        }

        public ViewCountConfig build() {
            if(median == null) {
                log.warn("조회수 중앙값이 설정되지 않아 {}으로 설정합니다", DEFAULT_MEDIAN);
                median = DEFAULT_MEDIAN;
            }
            if(std == null) {
                log.warn("조회수 표준편차가 설정되지 않아 {}으로 설정합니다", DEFAULT_HEURISTIC_FACTOR * median);
                std = DEFAULT_HEURISTIC_FACTOR * median;
            }
            if(criteria == null) {
                criteria = DEFAULT_CRITERIA;
            }
            return new ViewCountConfig(median, std, criteria);
        }
    }
}
