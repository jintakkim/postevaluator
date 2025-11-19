package com.jintakkim.postevaluator.feature;

import lombok.Getter;

import java.util.*;

@Getter
public class FeatureRegistry implements FeatureProvider {
    private final List<Feature> features = new ArrayList<>();

    public void register(Feature feature) {
        validateDuplicateName(feature.getName());
        features.add(feature);
    }

    private void validateDuplicateName(String featureName) {
        Optional<String> duplicationName = features.stream().map(Feature::getName).filter(featureName::equals).findAny();
        if(duplicationName.isPresent()) throw new IllegalArgumentException("이미 등록된 이름입니다: " + featureName);
    }

    public List<String> getFeatureNames() {
        return features.stream().map(Feature::getName).toList();
    }
}
