package com.jintakkim.postevaluator.config;

import com.jintakkim.postevaluator.core.DatasetManager;
import com.jintakkim.postevaluator.config.properties.DatasetProperties;
import com.jintakkim.postevaluator.core.PostGenerator;
import com.jintakkim.postevaluator.core.UserGenerator;
import com.jintakkim.postevaluator.core.labeling.Labeler;
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
                dbConfig.jdbi,
                datasetProperties.datasetSize(),
                datasetProperties.setupStrategy(),
                dbConfig.postRepository,
                dbConfig.userRepository,
                dbConfig.labelRepository,
                userGenerator,
                postGenerator,
                labeler
        );
    }

}
