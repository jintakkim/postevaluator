package com.jintakkim.postevaluator.core.persistance;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DynamicSqlGenerateUtilsTest {
    @Test
    @DisplayName("필드명이 스네이크 케이스로 변경되어야 한다.")
    void generateShouldConvertColumnNameToSnakeCase() {
        List<ColumnDefinition> columnDefinitions = List.of(new ColumnDefinition("unnamedColumn", SqlType.TEXT, true));
        String sql = DynamicSqlGenerateUtils.generateTable("test", columnDefinitions);
        Assertions.assertThat(sql).containsSubsequence("unnamed_column TEXT");
    }

    @Test
    @DisplayName("id가 포함되어야 한다.")
    void generateShouldAutomaticallyAddIdPrimaryKeyColumn() {
        List<ColumnDefinition> columnDefinitions = List.of(new ColumnDefinition("unnamedColumn", SqlType.TEXT, true));
        String sql = DynamicSqlGenerateUtils.generateTable("test", columnDefinitions);
        Assertions.assertThat(sql).containsSubsequence("id INTEGER PRIMARY KEY AUTOINCREMENT");
    }

    @Test
    @DisplayName("입력으로 들어온 columnDefination에 맞게 테이블 생성 Sql문을 작성한다.")
    void generateShouldProduceCorrectSqlForMultipleColumns() {
        List<ColumnDefinition> columnDefinitions = List.of(
                new ColumnDefinition("namedColumnA", SqlType.TEXT, true),
                new ColumnDefinition("namedColumnB", SqlType.TEXT, true)
        );
        String sql = DynamicSqlGenerateUtils.generateTable("test", columnDefinitions);
        Assertions.assertThat(sql).containsSubsequence(
                "CREATE TABLE test (",
                "id INTEGER PRIMARY KEY AUTOINCREMENT,",
                "named_column_a TEXT,",
                "named_column_b TEXT",
                ");"
        );
    }
}
