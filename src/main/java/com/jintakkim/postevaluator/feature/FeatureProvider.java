package com.jintakkim.postevaluator.feature;

import java.util.List;

public interface FeatureProvider {
    List<String> getFeatureNames();
    List<Feature> getFeatures();
}
