package com.jintakkim.postevaluator.core.distribution;

public record LogNormalDistribution(
        long median,
        double std
) implements Distribution {
    @Override
    public String getDescription() {
        return String.format("로그 정규 분포을 따른다, 중앙값은 %d이며, 표준편차는 %f이다.", median, std);
    }
}
