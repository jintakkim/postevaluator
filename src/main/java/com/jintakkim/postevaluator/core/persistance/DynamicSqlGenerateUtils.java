package com.jintakkim.postevaluator.core.persistance;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DynamicSqlGenerateUtils {
    private static final String PRIMARY_KEY_SQL = "id INTEGER PRIMARY KEY AUTOINCREMENT";

    public static String generateTable(String tableName, List<ColumnDefinition> columns) {
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

    public static String generateInsert(String tableName, Collection<String> columnNames) {
        String columnsPart = String.join(", ", columnNames);
        String valuesPart = columnNames.stream()
                .map(col -> ":" + col)
                .collect(Collectors.joining(", "));

        return String.format("INSERT INTO %s (%s) VALUES (%s)", tableName ,columnsPart, valuesPart);
    }

    public static String generateAlias(String alias, Collection<ColumnDefinition> columnDefinitions) {
        String idPart = String.format("%s.id AS %s_id", alias, alias);
        String otherParts = columnDefinitions.stream()
                .map(col -> String.format("%s.%s AS %s_%s", alias, col.name(), alias, col.name()))
                .collect(Collectors.joining(", "));
        return columnDefinitions.isEmpty() ? idPart : idPart + ", " + otherParts;
    }
}
