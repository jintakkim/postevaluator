package com.jintakkim.postevaluator.evaluation;

import java.util.Map;

/**
 * 평가 데이터, 라벨링된 데이터이다.
 */
public record LabeledPost(
        long id,
        Map<String, Object> features,
        double labeledScore
) implements FeatureAccessor {

    public <T> T getFeature(String featureName, Class<T> type) {
        return type.cast(features.get(featureName));
    }

    public Object getFeature(String featureName) {
        return features.get(featureName);
    }
}
