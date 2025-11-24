package com.jintakkim.postevaluator.config.properties;

import com.jintakkim.postevaluator.persistance.SetupStrategy;

public record DatasetProperties(
        int userDatasetSize,
        int postDatasetSize,
        SetupStrategy setupStrategy
) {
    public static final int DEFAULT_USER_DATASET_SIZE = 20;
    public static final int DEFAULT_POST_DATASET_SIZE = 20;
    public static final SetupStrategy DEFAULT_SETUP_STRATEGY = SetupStrategy.REUSE_STRICT;

    public DatasetProperties(int userDatasetSize, int postDatasetSize, SetupStrategy setupStrategy) {
        this.userDatasetSize = userDatasetSize;
        this.postDatasetSize = postDatasetSize;
        this.setupStrategy = setupStrategy;
        validateNotNull();
    }

    public static DatasetProperties defaults() {
        return new DatasetProperties(DEFAULT_USER_DATASET_SIZE, DEFAULT_POST_DATASET_SIZE, DEFAULT_SETUP_STRATEGY);
    }

    private void validateNotNull() {
        if(setupStrategy == null) throw new IllegalArgumentException("Dataset setup strategy가 null입니다.");
    }
}
