package com.jintakkim.postevaluator.core.infra;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DynamicTableGenerator {
    private static final String PRIMARY_KEY_SQL = "id INTEGER PRIMARY KEY AUTOINCREMENT";

    public static String generate(String tableName, List<ColumnDefinition> columns) {
        List<String> columnsSql = columns.stream()
                .map(ColumnDefinition::toColumnSql)
                .toList();
        String primaryKeyAdded = Stream.concat(
                Stream.of(PRIMARY_KEY_SQL),
                columnsSql.stream()
        ).collect(Collectors.joining(",\n\t"));

        return String.format(
                "CREATE TABLE %s (\n\t%s\n);",
                tableName,
                primaryKeyAdded
        );
    }
}
