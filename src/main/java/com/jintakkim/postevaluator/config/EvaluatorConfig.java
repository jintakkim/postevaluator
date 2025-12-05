package com.jintakkim.postevaluator.config;

import com.jintakkim.postevaluator.evaluation.Evaluator;
import com.jintakkim.postevaluator.evaluation.PipelinedEvaluator;
import com.jintakkim.postevaluator.evaluation.SimpleEvaluator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EvaluatorConfig {
    public final Evaluator evaluator;
    public final PipelinedEvaluator pipelinedEvaluator;

    public EvaluatorConfig(
            AlgorithmMetricConfig algorithmMetricConfig,
            DatasetConfig datasetConfig
    ) {
        this.evaluator = new SimpleEvaluator(algorithmMetricConfig.algorithmMetric, datasetConfig.datasetManager.getAll());
        this.pipelinedEvaluator = new PipelinedEvaluator(
                algorithmMetricConfig.algorithmMetric,
                datasetConfig.datasetManager.getAll()
        );
    }
}
