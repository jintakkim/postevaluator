package com.jintakkim.postevaluator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jintakkim.postevaluator.config.properties.AlgorithmMetricProperties;
import com.jintakkim.postevaluator.config.properties.DatasetProperties;
import com.jintakkim.postevaluator.config.properties.DefinitionProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationConfig {
    private final DbConfig dbConfig;
    private final DatasetConfig datasetConfig;
    private final GeminiConfig geminiConfig;
    private final ObjectMapper objectMapper;
    private final GeneratorConfig generatorConfig;
    private final LabelerConfig labelerConfig;
    private final AlgorithmMetricConfig algorithmMetricConfig;
    public final EvaluatorConfig evaluatorConfig;

    public ApplicationConfig(
            DefinitionProperties definitionProperties,
            DatasetProperties datasetProperties,
            AlgorithmMetricProperties algorithmMetricProperties
    ) {
        this.dbConfig = new DbConfig(definitionProperties);
        this.geminiConfig = new GeminiConfig();
        this.objectMapper = new ObjectMapper();
        this.generatorConfig = new GeneratorConfig(definitionProperties, geminiConfig, objectMapper);
        this.labelerConfig = new LabelerConfig(definitionProperties, geminiConfig, objectMapper);
        this.datasetConfig = new DatasetConfig(datasetProperties, dbConfig, generatorConfig, labelerConfig);
        this.algorithmMetricConfig = new AlgorithmMetricConfig(algorithmMetricProperties);
        this.evaluatorConfig = new EvaluatorConfig(algorithmMetricConfig, datasetConfig);
    }
}
