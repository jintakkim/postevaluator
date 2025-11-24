package com.jintakkim.postevaluator.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.jintakkim.postevaluator.config.properties.DefinitionProperties;
import com.jintakkim.postevaluator.generation.UserGenerator;
import com.jintakkim.postevaluator.gemini.GeminiUserGenerator;
import com.jintakkim.postevaluator.generation.PostGenerator;
import com.jintakkim.postevaluator.gemini.GeminiPostGenerator;

public class GeneratorConfig {
    public final PostGenerator postGenerator;
    public final UserGenerator userGenerator;

    public GeneratorConfig(
            DefinitionProperties definitionProperties,
            GeminiConfig geminiConfig,
            ObjectMapper objectMapper,
            ExecutorConfig executorConfig
    ) {
        this.postGenerator = new GeminiPostGenerator(geminiConfig.client, objectMapper, definitionProperties.postDefinition(), executorConfig.executor);
        this.userGenerator = new GeminiUserGenerator(geminiConfig.client, objectMapper, definitionProperties.userDefinition(), executorConfig.executor);
    }
}
