package com.jintakkim.postevaluator.persistance;

import com.jintakkim.postevaluator.FeatureType;
import lombok.Getter;

@Getter
public enum SqlType {
    INTEGER("INTEGER", Integer.class),
    LONG("INTEGER", Long.class),
    TEXT("TEXT", String.class),
    REAL("REAL", Double.class);

    private final String dbType;
    private final Class<?> javaType;

    SqlType(String dbType, Class<?> javaType) {
        this.dbType = dbType;
        this.javaType = javaType;
    }

    public static SqlType convertFeatureTypeToSqlType(FeatureType featureType) {
        return switch (featureType) {
            case INTEGER -> INTEGER;
            case LONG -> LONG;
            case STRING, DATE -> TEXT;
            case DOUBLE -> REAL;
        };
    }
};