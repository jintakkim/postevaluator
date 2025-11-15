package com.jintakkim.postevaluator.config;

import com.jintakkim.postevaluator.core.SetupStrategy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatasetConfig {
    private static final int MAX_DATASET_SIZE = 500;
    private static final int MIN_DATASET_SIZE = 10;
    private static final int DEFAULT_DATASET_SIZE = 30;
    private static final SetupStrategy DEFAULT_SETUP_STRATEGY = SetupStrategy.REUSE_STRICT;

    public final int targetDatasetSize;
    public final SetupStrategy setupStrategy;

    private DatasetConfig(int targetDatasetSize, SetupStrategy setupStrategy) {
        validateDatasetSize(targetDatasetSize);
        this.targetDatasetSize = targetDatasetSize;
        this.setupStrategy = setupStrategy;
    }

    public static Builder builder() {
        return new Builder();
    }

    private static void validateDatasetSize(int datasetSize) {
        if (datasetSize < MIN_DATASET_SIZE || datasetSize > MAX_DATASET_SIZE)
            throw new IllegalArgumentException("데이터 셋 사이즈는 " + MIN_DATASET_SIZE + "에서" + MAX_DATASET_SIZE + "사이 이여야합니다.");
    }

    public static class Builder {
        private Integer targetDatasetSize;
        private SetupStrategy setupStrategy;

        public Builder targetDatasetSize(int targetDatasetSize) {
            this.targetDatasetSize = targetDatasetSize;
            return this;
        }

        public Builder setupStrategy(SetupStrategy setupStrategy) {
            this.setupStrategy = setupStrategy;
            return this;
        }

        public DatasetConfig build() {
            if(targetDatasetSize == null) {
                log.info("평가에 사용할 데이터셋 사이즈를 지정하지 않았습니다 기본값 : {}로 지정합니다", DEFAULT_DATASET_SIZE);
                targetDatasetSize = DEFAULT_DATASET_SIZE;
;            }
            if(setupStrategy == null) {
                log.info("데이터 셋업 전략이 설정되지 않았습니다 기본값: {}로 지정합니다", DEFAULT_SETUP_STRATEGY);
            }
            return new DatasetConfig(targetDatasetSize, setupStrategy);
        }
    }



}
