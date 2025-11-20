package com.jintakkim.postevaluator.evaluation;

public interface FeatureAccessor {
    <T> T getFeature(String featureName, Class<T> type);
    Object getFeature(String featureName);
}
