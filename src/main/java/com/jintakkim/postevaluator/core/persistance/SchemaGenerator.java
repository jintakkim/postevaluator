package com.jintakkim.postevaluator.core.persistance;

import java.util.List;

public interface SchemaGenerator {
    void generate(List<ColumnDefinition> columns);
}
