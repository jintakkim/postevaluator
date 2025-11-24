package com.jintakkim.postevaluator.feature;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NumberTypeGenerationCriteriaBuilder {

    public static MinMaxBuilder minMaxBuilder() {
        return new MinMaxBuilder();
    }

    public static LogNormalDistributionBuilder logNormalDistributionBuilder() {
        return new LogNormalDistributionBuilder();
    }

    public static class MinMaxBuilder extends GenerationCriteriaBuilder {
        private Double min;
        private Double max;

        public MinMaxBuilder min(double min) {
            this.min = min;
            return this;
        }

        public MinMaxBuilder max(double max) {
            this.max = max;
            return this;
        }

        public GenerationCriteria build() {
            validate(min, max);
            if(min == null) {
            }
            attributes.put("min", String.valueOf(min));
            if(max != null) attributes.put("max", String.valueOf(max));
            return super.build();
        }

        private void validate(Double min, Double max) {
            if (min != null && max != null && max < min) throw new IllegalArgumentException("최소값은 최대값보다 작아야합니다.");
        }
    }

    public static class LogNormalDistributionBuilder extends GenerationCriteriaBuilder {
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
            attributes.put("distribution", DISTRIBUTION_NAME);
            if(median != null) {
                attributes.put("median", String.valueOf(median));
            }
            if(std != null) {
                attributes.put("std", String.valueOf(std));
            }
            return super.build();
        }
    }
}
