package com.jintakkim.postevaluator.test;

import com.jintakkim.postevaluator.feature.Feature;
import com.jintakkim.postevaluator.feature.FeatureProvider;

import java.util.List;

public final class TestFeatureProviderGenerator {
    private TestFeatureProviderGenerator() {}

    public static FeatureProvider generate(List<Feature> features) {
        return new FeatureProvider() {

            @Override
            public List<String> getFeatureNames() {
                return features.stream().map(Feature::getName).toList();
            }

            @Override
            public List<Feature> getFeatures() {
                return features;
            }
        };
    }
}
