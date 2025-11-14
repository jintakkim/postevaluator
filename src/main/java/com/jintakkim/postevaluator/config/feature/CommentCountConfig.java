package com.jintakkim.postevaluator.config.feature;

import com.jintakkim.postevaluator.core.FeatureProperty;
import com.jintakkim.postevaluator.core.FeaturePropertyProvider;
import com.jintakkim.postevaluator.core.FeatureType;
import com.jintakkim.postevaluator.core.distribution.Distribution;
import com.jintakkim.postevaluator.core.distribution.LogNormalDistribution;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommentCountConfig implements FeaturePropertyProvider {
    private static final long DEFAULT_MEDIAN = 10L;
    private static final double DEFAULT_HEURISTIC_FACTOR = 0.5;
    private static final String DEFAULT_CRITERIA = """
            댓글 수는 사용자의 관심도와 콘텐츠에 대한 참여도를 나타내는 지표.
            """;

    private final Distribution distribution;
    private final String criteria;

    public CommentCountConfig(long median, double std, String criteria) {
        if (median < 0 || std < 0) {
            throw new IllegalArgumentException("중앙값과 표준편차는 0 이상이어야 합니다.");
        }
        this.distribution = new LogNormalDistribution(median, std);
        this.criteria = criteria;
    }

    public static CommentCountConfig.Builder builder() {
        return new CommentCountConfig.Builder();
    }

    public String getCriteria() {
        return criteria;
    }

    @Override
    public FeatureProperty getFeatureProperty() {
        return new FeatureProperty() {
            @Override
            public String getName() {
                return "commentCount";
            }

            @Override
            public String getFeatureDescription() {
                return String.format("댓글 수이다, 통계적 특성: %s", distribution.getDescription());
            }

            @Override
            public FeatureType getFeatureType() {
                return FeatureType.INTEGER;
            }
        };
    }

    public static class Builder {
        private Long median;
        private Double std;
        private String criteria;

        public CommentCountConfig.Builder criteria(String criteria) {
            this.criteria = criteria;
            return this;
        }

        public CommentCountConfig.Builder median(long median) {
            this.median = median;
            return this;
        }

        public CommentCountConfig.Builder std(double std) {
            this.std = std;
            return this;
        }

        public CommentCountConfig build() {
            if(median == null) {
                log.warn("댓글 수 중앙값이 설정되지 않아 {}으로 설정합니다", DEFAULT_MEDIAN);
                median = DEFAULT_MEDIAN;
            }
            if(std == null) {
                log.warn("댓글 수 표준편차가 설정되지 않아 {}으로 설정합니다", DEFAULT_HEURISTIC_FACTOR * median);
                std = DEFAULT_HEURISTIC_FACTOR * median;
            }
            if(criteria == null) {
                criteria = DEFAULT_CRITERIA;
            }
            return new CommentCountConfig(median, std, criteria);
        }
    }
}
