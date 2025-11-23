package com.jintakkim.postevaluator.config;

import com.jintakkim.postevaluator.persistance.DatasetManager;
import com.jintakkim.postevaluator.config.properties.DatasetProperties;
import com.jintakkim.postevaluator.generation.PostGenerator;
import com.jintakkim.postevaluator.generation.UserGenerator;
import com.jintakkim.postevaluator.labeling.Labeler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatasetConfig {
    public final DatasetManager datasetManager;

    public DatasetConfig(
            DatasetProperties datasetProperties,
            DbConfig dbConfig,
            UserGenerator userGenerator,
            PostGenerator postGenerator,
            Labeler labeler
    ) {
        this.datasetManager = new DatasetManager(
                dbConfig.jdbiContext,
                datasetProperties.datasetSize(),
                datasetProperties.setupStrategy(),
                dbConfig.postRepository,
                dbConfig.userRepository,
                dbConfig.labelRepository,
                dbConfig.sampleRepository,
                userGenerator,
                postGenerator,
                labeler
        );
    }

}
