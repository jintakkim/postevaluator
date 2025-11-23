package com.jintakkim.postevaluator.core;

import com.jintakkim.postevaluator.core.persistance.ColumnDefinition;

import java.util.List;
import java.util.Map;

public interface EntitySchema {
    String getDefinitionHash();
    List<ColumnDefinition> getColumnDefinitions();
    /**
     * Key: Feature Name (CamelCase) - 예: likeCount
     * Value: Column Name (SnakeCase) - 예: like_count
     */
    Map<String, String> getFieldToColumnMap();
    /**
     * key: Column Name (SnakeCase) - 예: like_count
     * value: Feature Name (CamelCase) - 예: likeCount
     */
    Map<String, String> getColumnToFieldMap();
}
