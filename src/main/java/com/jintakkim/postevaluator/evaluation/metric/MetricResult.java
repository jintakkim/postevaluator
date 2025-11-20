package com.jintakkim.postevaluator.evaluation.metric;

import java.util.List;

public record MetricResult(
        List<Long> topErrorOccurredPostIds,
        double cost
) {
}
