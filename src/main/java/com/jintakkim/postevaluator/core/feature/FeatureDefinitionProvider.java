package com.jintakkim.postevaluator.core.feature;

import java.util.List;

public interface FeatureDefinitionProvider {
    List<String> getFeatureNames();
    List<FeatureDefinition> getFeatureDefinitions();
}
