package com.jintakkim.postevaluator.config;

import com.jintakkim.postevaluator.evaluation.metric.AlgorithmMetric;
import com.jintakkim.postevaluator.evaluation.metric.MSEMetric;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlgorithmMetricConfig {
    private static final AlgorithmMetric DEFAULT_METRIC = new MSEMetric();

    public final AlgorithmMetric algorithmMetric;

    private AlgorithmMetricConfig(AlgorithmMetric algorithmMetric) {
        this.algorithmMetric = algorithmMetric;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private AlgorithmMetric algorithmMetric;

        public Builder algorithmMetric(AlgorithmMetric algorithmMetric) {
            this.algorithmMetric = algorithmMetric;
            return this;
        }

        public AlgorithmMetricConfig build() {
            if(algorithmMetric == null) {
                log.info("평가 지표 설정을 하지 않았습니다, 기본값: {}으로 설정합니다", DEFAULT_METRIC.getName());
                this.algorithmMetric = DEFAULT_METRIC;
            }
            
            return new AlgorithmMetricConfig(algorithmMetric);
        }
    }
}
