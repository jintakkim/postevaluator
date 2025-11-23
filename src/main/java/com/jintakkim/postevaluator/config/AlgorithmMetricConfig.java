package com.jintakkim.postevaluator.config;

import com.jintakkim.postevaluator.config.properties.AlgorithmMetricProperties;
import com.jintakkim.postevaluator.evaluation.metric.AlgorithmMetric;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlgorithmMetricConfig {
    public final AlgorithmMetric algorithmMetric;

    public AlgorithmMetricConfig(AlgorithmMetricProperties algorithmMetricProperties) {
        this.algorithmMetric = algorithmMetricProperties.algorithmMetric();
    }
}
