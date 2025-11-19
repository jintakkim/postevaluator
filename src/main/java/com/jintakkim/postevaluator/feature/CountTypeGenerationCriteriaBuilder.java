package com.jintakkim.postevaluator.feature;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CountTypeGenerationCriteriaBuilder {
    private static final Long MIN_COUNT_VALUE = 0L;

    public static MinMaxBuilder minMaxBuilder() {
        return new MinMaxBuilder();
    }

    public static LogNormalDistributionBuilder logNormalDistributionBuilder() {
        return new LogNormalDistributionBuilder();
    }

    public static class MinMaxBuilder {
        private Long min;
        private Long max;

        public MinMaxBuilder min(long min) {
            this.min = min;
            return this;
        }

        public MinMaxBuilder max(long max) {
            this.max = max;
            return this;
        }

        public GenerationCriteria build() {
            validate(min, max);
            Map<String, String> attributes = new HashMap<>();
            if(min == null) {
                log.info("최소값을 지정하지 않았기 떄문에 {}으로 설정합니다.", MIN_COUNT_VALUE);
                min = MIN_COUNT_VALUE;
            }
            attributes.put("min", String.valueOf(min));
            if(max != null) attributes.put("max", String.valueOf(max));
            return new GenerationCriteria(attributes);
        }

        private void validate(Long min, Long max) {
            if (min != null && min < 0) throw new IllegalArgumentException("최소값은 0보다 커야합니다.");
            if (max != null && max < 0) throw new IllegalArgumentException("최대값은 0보다 커야합니다.");
            if (min != null && max != null && max < min) throw new IllegalArgumentException("최소값은 최대값보다 작아야합니다.");
        }
    }

    public static class LogNormalDistributionBuilder {
        private static final String DISTRIBUTION_NAME = "logNormal";
        private Long median;
        private Double std;

        public LogNormalDistributionBuilder median(Long median) {
            this.median = median;
            return this;
        }

        public LogNormalDistributionBuilder std(Double std) {
            this.std = std;
            return this;
        }

        public GenerationCriteria build() {
            if(median != null) log.warn("중앙값을 지정하지 않았습니다.");
            if(std != null) log.warn("표준편차를 지정하지 않았습니다.");

            Map<String, String> attributes = new HashMap<>();
            attributes.put("distribution", DISTRIBUTION_NAME);
            attributes.put("min", String.valueOf(MIN_COUNT_VALUE));
            if(median != null) {
                attributes.put("median", String.valueOf(median));
            }
            if(std != null) {
                attributes.put("std", String.valueOf(std));
            }
            return new GenerationCriteria(attributes);
        }
    }
}
