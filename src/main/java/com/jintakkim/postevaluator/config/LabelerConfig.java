package com.jintakkim.postevaluator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jintakkim.postevaluator.config.properties.DefinitionProperties;
import com.jintakkim.postevaluator.gemini.BatchGeminiLabeler;
import com.jintakkim.postevaluator.labeling.Labeler;

public class LabelerConfig {
    public final Labeler labeler;

    public LabelerConfig(
            DefinitionProperties definitionProperties,
            GeminiConfig geminiConfig,
            ObjectMapper objectMapper,
            ExecutorConfig executorConfig
    ) {
        this.labeler = new BatchGeminiLabeler(
                definitionProperties.userDefinition(),
                definitionProperties.postDefinition(),
                geminiConfig.client,
                objectMapper,
                executorConfig.executor
        );
    }
}
