package com.jintakkim.postevaluator.test;

import com.jintakkim.postevaluator.feature.FeatureDefinition;
import com.jintakkim.postevaluator.feature.FeatureDefinitionProvider;

import java.util.List;

public final class TestFeatureDefinitionProviderGenerator {
    private TestFeatureDefinitionProviderGenerator() {}

    public static FeatureDefinitionProvider generate(List<FeatureDefinition> featureDefinitions) {
        return new FeatureDefinitionProvider() {

            @Override
            public List<String> getFeatureNames() {
                return featureDefinitions.stream().map(FeatureDefinition::name).toList();
            }

            @Override
            public List<FeatureDefinition> getFeatureDefinitions() {
                return featureDefinitions;
            }
        };
    }
}
