package com.jintakkim.postevaluator.config;

import com.jintakkim.postevaluator.feature.Feature;
import com.jintakkim.postevaluator.feature.FeatureRegistry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeatureConfig {
    public final FeatureRegistry featureRegistry;

    private FeatureConfig(
            FeatureRegistry featureRegistry
    ) {
        this.featureRegistry = featureRegistry;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final FeatureRegistry featureRegistry = new FeatureRegistry();

        public Builder addFeature(Feature feature) {
            featureRegistry.register(feature);
            return this;
        }

        public FeatureConfig build() {
            if(featureRegistry.getFeatures().isEmpty()) {
                throw new IllegalArgumentException("아무 Feature도 등록되지 않았습니다. 적어도 한개를 등록해야 합니다.");
            }
            return new FeatureConfig(featureRegistry);
        }
    }
}
