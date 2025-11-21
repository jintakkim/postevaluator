package com.jintakkim.postevaluator.config;

import com.jintakkim.postevaluator.feature.FeatureDefinition;
import com.jintakkim.postevaluator.feature.FeatureDefinitionRegistry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeatureConfig {
    public final FeatureDefinitionRegistry featureRegistry;

    private FeatureConfig(
            FeatureDefinitionRegistry featureRegistry
    ) {
        this.featureRegistry = featureRegistry;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final FeatureDefinitionRegistry featureRegistry = new FeatureDefinitionRegistry();

        public Builder addFeature(FeatureDefinition featureDefinition) {
            featureRegistry.register(featureDefinition);
            return this;
        }

        public FeatureConfig build() {
            if(featureRegistry.getFeatureDefinitions().isEmpty()) {
                throw new IllegalArgumentException("아무 Feature도 등록되지 않았습니다. 적어도 한개를 등록해야 합니다.");
            }
            return new FeatureConfig(featureRegistry);
        }
    }
}
