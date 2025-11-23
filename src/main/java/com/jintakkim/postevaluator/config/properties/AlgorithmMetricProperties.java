package com.jintakkim.postevaluator.config.properties;

import com.jintakkim.postevaluator.evaluation.metric.AlgorithmMetric;
import com.jintakkim.postevaluator.evaluation.metric.MSEMetric;
import com.jintakkim.postevaluator.evaluation.metric.RMSEMetric;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

public record AlgorithmMetricProperties(AlgorithmMetric algorithmMetric) {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class BuiltIn {
        public static final AlgorithmMetric MEAN_SQUARED_ERROR = new MSEMetric();
        public static final AlgorithmMetric ROOT_MEAN_SQUARED_ERROR = new RMSEMetric();
    }
}
