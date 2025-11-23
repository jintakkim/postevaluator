package com.jintakkim.postevaluator.config.properties;

import com.jintakkim.postevaluator.SetupStrategy;

public record DatasetProperties(
        int datasetSize,
        SetupStrategy setupStrategy
) {
    private static final int DEFAULT_DATASET_SIZE = 30;
    private static final SetupStrategy DEFAULT_SETUP_STRATEGY = SetupStrategy.REUSE_STRICT;

    public DatasetProperties(int datasetSize, SetupStrategy setupStrategy) {
        this.datasetSize = datasetSize;
        this.setupStrategy = setupStrategy;
        validateNotNull();
    }

    private void validateNotNull() {
        if(setupStrategy == null) throw new IllegalArgumentException("Dataset setup strategy가 null입니다.");
    }
}
