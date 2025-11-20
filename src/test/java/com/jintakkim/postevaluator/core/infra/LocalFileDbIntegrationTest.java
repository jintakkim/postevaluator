package com.jintakkim.postevaluator.core.infra;

import com.jintakkim.postevaluator.core.LabeledPost;
import com.jintakkim.postevaluator.core.Post;
import com.jintakkim.postevaluator.feature.Feature;
import com.jintakkim.postevaluator.fixture.FeatureFixture;
import com.jintakkim.postevaluator.test.TestDbConfig;
import com.jintakkim.postevaluator.test.TestFeatureProviderGenerator;
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
    protected static final List<Feature> TEST_FEATURES = List.of(FeatureFixture.COMMENT_COUNT, FeatureFixture.LIKE_COUNT);

    protected static TestDbConfig dbConfig;
    protected static LabeledPostRepository labeledPostRepository;
    protected static PostRepository postRepository;
    protected static Jdbi jdbi;
    protected static PostDatabaseSynchronizer postTableSynchronizer;

    @BeforeAll
    static void initialize() {
        dbConfig = TestDbConfig.withSetting(TestFeatureProviderGenerator.generate(TEST_FEATURES) ,true);
        jdbi = dbConfig.jdbi;
        labeledPostRepository = dbConfig.labeledPostRepository;
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
                FeatureFixture.COMMENT_COUNT.getName(), 10L,
                FeatureFixture.LIKE_COUNT.getName(), 100L
        ));
    }

    static LabeledPost createLabeledPost(Long featureId) {
        return new LabeledPost(featureId, 1.0, "reason??");
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
