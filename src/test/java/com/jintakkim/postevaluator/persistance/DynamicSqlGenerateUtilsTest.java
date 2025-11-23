package com.jintakkim.postevaluator.persistance;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DynamicSqlGenerateUtilsTest {
    @Nested
    @DisplayName("테이블 생성 테스트")
    class TableSqlCreationTest {
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

    @Test
    @DisplayName("insert문 문법에 맞춰 작성 해야한다.")
    void shouldGenerateInsertSqlWithNamedParameters() {
        String insertSql = DynamicSqlGenerateUtils.generateInsert("test", List.of("column_a", "column_b"));
        Assertions.assertThat(insertSql)
                .isEqualTo("INSERT INTO test (column_a, column_b) VALUES (column_a:column_a, column_b:column_b)");
    }

    @Test
    @DisplayName("alias를 설정 한다.")
    void shouldCreateValidJdbiInsertStatement() {
        String aliasApplied = DynamicSqlGenerateUtils.generateAlias("t", List.of(
                new ColumnDefinition("columnA", SqlType.TEXT, false),
                new ColumnDefinition("columnB", SqlType.TEXT, false)
        ));

        Assertions.assertThat(aliasApplied)
                .isEqualTo("t.id AS t_id, t.column_a AS t_column_a, t.column_b AS t_column_b");
    }
}
