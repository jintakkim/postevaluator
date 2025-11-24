package com.jintakkim.postevaluator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jintakkim.postevaluator.config.properties.AlgorithmMetricProperties;
import com.jintakkim.postevaluator.config.properties.DatasetProperties;
import com.jintakkim.postevaluator.config.properties.DefinitionProperties;

import com.jintakkim.postevaluator.config.properties.SearchProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationConfig {
    public final ExecutorConfig executorConfig;
    public final DbConfig dbConfig;
    public final DatasetConfig datasetConfig;
    public final GeminiConfig geminiConfig;
    public final ObjectMapper objectMapper;
    public final GeneratorConfig generatorConfig;
    public final LabelerConfig labelerConfig;
    public final AlgorithmMetricConfig algorithmMetricConfig;
    public final EvaluatorConfig evaluatorConfig;
    public final SearchConfig searchConfig;

    public ApplicationConfig(
            DefinitionProperties definitionProperties,
            DatasetProperties datasetProperties,
            AlgorithmMetricProperties algorithmMetricProperties,
            SearchProperties searchProperties
    ) {
        this.executorConfig = new ExecutorConfig();
        this.dbConfig = new DbConfig(definitionProperties);
        dbConfig.initialize();
        this.geminiConfig = new GeminiConfig();
        this.objectMapper = new ObjectMapper();
        this.generatorConfig = new GeneratorConfig(definitionProperties, geminiConfig, objectMapper, executorConfig);
        this.labelerConfig = new LabelerConfig(definitionProperties, geminiConfig, objectMapper, executorConfig);
        this.datasetConfig = new DatasetConfig(datasetProperties, dbConfig, generatorConfig, labelerConfig);
        datasetConfig.datasetManager.initializeDataset();
        this.algorithmMetricConfig = new AlgorithmMetricConfig(algorithmMetricProperties);
        this.evaluatorConfig = new EvaluatorConfig(algorithmMetricConfig, datasetConfig);
        this.searchConfig = new SearchConfig(searchProperties, evaluatorConfig);
    }

    public ApplicationConfig(
            DefinitionProperties definitionProperties,
            DatasetProperties datasetProperties,
            AlgorithmMetricProperties algorithmMetricProperties
    ) {
        this(definitionProperties, datasetProperties, algorithmMetricProperties, null);
    }
}
