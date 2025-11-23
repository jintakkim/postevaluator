package com.jintakkim.postevaluator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jintakkim.postevaluator.config.properties.DefinitionProperties;
import com.jintakkim.postevaluator.gemini.SequentialGeminiLabeler;
import com.jintakkim.postevaluator.labeling.Labeler;

public class LabelerConfig {
    public final Labeler labeler;

    public LabelerConfig(
            DefinitionProperties definitionProperties,
            GeminiConfig geminiConfig,
            ObjectMapper objectMapper
    ) {
        this.labeler = new SequentialGeminiLabeler(
                definitionProperties.userDefinition,
                definitionProperties.postDefinition,
                geminiConfig.client,
                objectMapper
        );
    }
}
