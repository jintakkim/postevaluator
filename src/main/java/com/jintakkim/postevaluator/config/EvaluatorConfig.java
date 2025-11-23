package com.jintakkim.postevaluator.config;

import com.jintakkim.postevaluator.core.DatasetManager;
import com.jintakkim.postevaluator.evaluation.BulkEvaluator;
import com.jintakkim.postevaluator.evaluation.Evaluator;
import com.jintakkim.postevaluator.evaluation.SimpleEvaluator;
import com.jintakkim.postevaluator.evaluation.metric.AlgorithmMetric;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EvaluatorConfig {
    public final Evaluator evaluator;
    public final BulkEvaluator bulkEvaluator;

    public EvaluatorConfig(
            AlgorithmMetric algorithmMetric,
            DatasetManager datasetManager
    ) {
        this.evaluator = new SimpleEvaluator(algorithmMetric, datasetManager.getLabeledPosts());
        this.bulkEvaluator = new BulkEvaluator(evaluator);
    }


}
