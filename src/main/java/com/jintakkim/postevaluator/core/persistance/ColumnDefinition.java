package com.jintakkim.postevaluator.core.persistance;

import com.google.common.base.CaseFormat;

public record ColumnDefinition(String name, SqlType type, boolean nullable) {
    private static final String NOT_NULL_CONDITION = " NOT NULL";

    public ColumnDefinition(String name, SqlType type, boolean nullable) {
        this.name = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
        this.type = type;
        this.nullable = nullable;
    }

    public String toColumnSql() {
        String sql = String.format("%s %s", name, type.toString());
        if (!nullable) sql += NOT_NULL_CONDITION;
        return sql;
    }
}