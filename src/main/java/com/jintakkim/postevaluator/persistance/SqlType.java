package com.jintakkim.postevaluator.persistance;

import com.jintakkim.postevaluator.FeatureType;

public enum SqlType {
    INTEGER, TEXT, REAL;

    public static SqlType convertFeatureTypeToSqlType(FeatureType featureType) {
        return switch (featureType) {
            case INTEGER -> INTEGER;
            case STRING -> TEXT;
            case DOUBLE -> REAL;
            case DATE -> TEXT;
        };
    }
};