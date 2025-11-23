package com.jintakkim.postevaluator.config;

import com.jintakkim.postevaluator.persistance.DatasetManager;
import com.jintakkim.postevaluator.config.properties.DatasetProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatasetConfig {
    public final DatasetManager datasetManager;

    public DatasetConfig(
            DatasetProperties datasetProperties,
            DbConfig dbConfig,
            GeneratorConfig generatorConfig,
            LabelerConfig labelerConfig
    ) {
        this.datasetManager = new DatasetManager(
                dbConfig.jdbiContext,
                datasetProperties.datasetSize(),
                datasetProperties.setupStrategy(),
                dbConfig.postRepository,
                dbConfig.userRepository,
                dbConfig.labelRepository,
                dbConfig.sampleRepository,
                generatorConfig.userGenerator,
                generatorConfig.postGenerator,
                labelerConfig.labeler
        );
    }

}
