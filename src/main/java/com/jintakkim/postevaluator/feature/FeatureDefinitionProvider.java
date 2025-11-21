package com.jintakkim.postevaluator.feature;

import java.util.List;

public interface FeatureDefinitionProvider {
    List<String> getFeatureNames();
    List<FeatureDefinition> getFeatureDefinitions();
}
