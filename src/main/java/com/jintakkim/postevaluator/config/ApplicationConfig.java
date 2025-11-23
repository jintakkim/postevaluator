package com.jintakkim.postevaluator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jintakkim.postevaluator.config.properties.DefinitionProperties;
import com.jintakkim.postevaluator.persistance.DatasetManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationConfig {
    private final DbConfig dbConfig;
    private final GeminiConfig geminiConfig;
    private final ObjectMapper objectMapper;
    private final GeneratorConfig generatorConfig;
    private final LabelerConfig labelerConfig;
    private final AlgorithmMetricConfig algorithmMetricConfig;
    private final DatasetManager datasetManager;
    public final EvaluatorConfig evaluatorConfig;

    private ApplicationConfig(
            DefinitionProperties definitionProperties,
            FeatureConfig featureConfig,
            DatasetConfig datasetConfig,
            AlgorithmMetricConfig algorithmMetricConfig
    ) {
        this.dbConfig = new DbConfig(definitionProperties);
        this.geminiConfig = new GeminiConfig();
        this.objectMapper = new ObjectMapper();
        this.generatorConfig = new GeneratorConfig(definitionProperties, geminiConfig, objectMapper);
        this.labelerConfig = new LabelerConfig(featureConfig.featureRegistry, geminiConfig, objectMapper);
        this.algorithmMetricConfig = algorithmMetricConfig;
      
        datasetManager.initializeDataset();
        this.evaluatorConfig = new EvaluatorConfig(algorithmMetricConfig.algorithmMetric, datasetManager);
    }

    public static class Builder {
        private DatasetConfig datasetConfig;
        private FeatureConfig featureConfig;
        private AlgorithmMetricConfig algorithmMetricConfig;

        public Builder datasetConfig(DatasetConfig datasetConfig) {
            this.datasetConfig = datasetConfig;
            return this;
        }

        public Builder featureConfig(FeatureConfig featureConfig) {
            this.featureConfig = featureConfig;
            return this;
        }

        public Builder algorithmMetricConfig(AlgorithmMetricConfig algorithmMetricConfig) {
            this.algorithmMetricConfig = algorithmMetricConfig;
            return this;
        }

        public ApplicationConfig build() {
            if(datasetConfig == null) this.datasetConfig = DatasetConfig.builder().build();
            if(featureConfig == null) this.featureConfig = FeatureConfig.builder().build();
            if(algorithmMetricConfig == null) this.algorithmMetricConfig = AlgorithmMetricConfig.builder().build();
            return new ApplicationConfig(featureConfig, datasetConfig, algorithmMetricConfig);
        }
    }
}
