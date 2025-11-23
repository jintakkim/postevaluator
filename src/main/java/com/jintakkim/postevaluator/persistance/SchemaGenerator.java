package com.jintakkim.postevaluator.persistance;

import java.util.List;

public interface SchemaGenerator {
    void generate(List<ColumnDefinition> columns);
}
