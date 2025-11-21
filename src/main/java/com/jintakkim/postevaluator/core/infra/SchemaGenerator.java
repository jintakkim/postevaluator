package com.jintakkim.postevaluator.core.infra;

import java.util.List;

public interface SchemaGenerator {
    void generate(List<ColumnDefinition> columns);
}
