package com.jintakkim.postevaluator.persistance;

import com.jintakkim.postevaluator.config.DbConfig;
import com.jintakkim.postevaluator.config.properties.DefinitionProperties;
import com.jintakkim.postevaluator.Label;
import com.jintakkim.postevaluator.Post;
import com.jintakkim.postevaluator.feature.FeatureDefinition;
import com.jintakkim.postevaluator.persistance.LabelRepository;
import com.jintakkim.postevaluator.persistance.PostRepository;
import com.jintakkim.postevaluator.persistance.initialization.TableDefinitionHashDatabaseInitializer;
import com.jintakkim.postevaluator.fixture.FeatureFixture;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.List;
import java.util.Map;

/**
 * 실제 로컬 파일에 저장이 되는 테스트
 */
@Slf4j
@Execution(ExecutionMode.SAME_THREAD) //기본값이지만 db 데이터에 의해 테스트간 격리가 깨질 수 있으므로 명시
public abstract class LocalFileDbIntegrationTest {
    protected static final List<FeatureDefinition> TEST_FEATURE_DEFINITIONS = List.of(FeatureFixture.COMMENT_COUNT, FeatureFixture.LIKE_COUNT);

    protected static DbConfig dbConfig;
    protected static LabelRepository labelRepository;
    protected static PostRepository postRepository;
    protected static Jdbi jdbi;
    protected static TableDefinitionHashDatabaseInitializer userInitializer;
    protected static TableDefinitionHashDatabaseInitializer postInitializer;

    @BeforeAll
    static void initialize() {
        dbConfig = new DbConfig("test.db", new DefinitionProperties());
        jdbi = dbConfig.jdbi;
        labelRepository = dbConfig.labelRepository;
        postRepository = dbConfig.postRepository;
        postTableSynchronizer = dbConfig.postTableSynchronizer;
    }

    static void rollbackTest(Executable executable) {
        jdbi.useTransaction(handle -> {
            executable.execute(handle);
            handle.rollback();
        });
    }

    @FunctionalInterface
    interface Executable {
        void execute(Handle handle);
    }

    static Post createPost() {
        return new Post(null, Map.of(
                FeatureFixture.COMMENT_COUNT.name(), 10L,
                FeatureFixture.LIKE_COUNT.name(), 100L
        ));
    }

    static Label createLabeledPost(Long featureId) {
        return new Label(featureId, 1.0, "reason??");
    }

    @AfterAll
    static void cleanupTables() {
        dbConfig.cleanupTables();
    }

    static boolean tableExists(String tableName) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT name FROM sqlite_master WHERE type='table' AND name = :tableName")
                        .bind("tableName", tableName)
                        .mapTo(String.class)
                        .findFirst()
                        .isPresent()
        );
    }

    static boolean columnExists(String tableName, String columnName) {
        return jdbi.withHandle(handle -> {
            List<Map<String, Object>> columnsInfo = handle
                    .createQuery("PRAGMA table_info(" + tableName + ")")
                    .mapToMap()
                    .list();
            return columnsInfo.stream()
                    .anyMatch(row -> row.get("name").equals(columnName));
        });
    }
}
