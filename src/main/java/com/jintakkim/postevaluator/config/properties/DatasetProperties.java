package com.jintakkim.postevaluator.config.properties;

import com.jintakkim.postevaluator.persistance.SetupStrategy;

public record DatasetProperties(
        int datasetSize,
        SetupStrategy setupStrategy
) {
    public static final int DEFAULT_DATASET_SIZE = 100;
    public static final SetupStrategy DEFAULT_SETUP_STRATEGY = SetupStrategy.REUSE_STRICT;

    public DatasetProperties(int datasetSize, SetupStrategy setupStrategy) {
        this.datasetSize = datasetSize;
        this.setupStrategy = setupStrategy;
        validateNotNull();
    }

    public DatasetProperties() {
        this(DEFAULT_DATASET_SIZE, DEFAULT_SETUP_STRATEGY);
    }

    private void validateNotNull() {
        if(setupStrategy == null) throw new IllegalArgumentException("Dataset setup strategy가 null입니다.");
    }
}
