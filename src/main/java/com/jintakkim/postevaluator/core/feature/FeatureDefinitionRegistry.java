package com.jintakkim.postevaluator.core.feature;

import lombok.Getter;

import java.util.*;

@Getter
public class FeatureDefinitionRegistry implements FeatureDefinitionProvider {
    private final List<FeatureDefinition> featureDefinitions = new ArrayList<>();

    public void register(FeatureDefinition featureDefinition) {
        validateDuplicateName(featureDefinition.name());
        featureDefinitions.add(featureDefinition);
    }

    private void validateDuplicateName(String featureName) {
        Optional<String> duplicationName = featureDefinitions.stream().map(FeatureDefinition::name).filter(featureName::equals).findAny();
        if(duplicationName.isPresent()) throw new IllegalArgumentException("이미 등록된 이름입니다: " + featureName);
    }

    public List<String> getFeatureNames() {
        return featureDefinitions.stream().map(FeatureDefinition::name).toList();
    }
}
