package com.jintakkim.postevaluator.core.infra;

import com.google.common.base.CaseFormat;
import com.jintakkim.postevaluator.core.FeatureType;
import com.jintakkim.postevaluator.feature.Feature;

import java.util.List;

public interface SchemaGenerator {
    void generate(List<ColumnDefinition> columns);

    record ColumnDefinition(String name, SqlType type, boolean nullable) {
        private static final String NOT_NULL_CONDITION = " NOT NULL";

        public static ColumnDefinition from(Feature feature) {
            return new ColumnDefinition(
                    CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, feature.getName()),
                    SqlType.convertFeatureTypeToSqlType(feature.getType()),
                    false
            );
        }

        public String toColumnSql() {
            String sql = String.format("%s %s", name, type.toString());
            if (!nullable) sql += NOT_NULL_CONDITION;
            return sql;
        }
    }

    enum SqlType {
        INTEGER, TEXT, REAL;

        public static SqlType convertFeatureTypeToSqlType(FeatureType featureType) {
            return switch (featureType) {
                case INTEGER -> INTEGER;
                case STRING -> TEXT;
                case DOUBLE -> REAL;
                case DATE -> TEXT;
            };
        }
    }
}
