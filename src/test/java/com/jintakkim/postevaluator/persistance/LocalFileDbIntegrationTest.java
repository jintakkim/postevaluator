package com.jintakkim.postevaluator.persistance;

import com.jintakkim.postevaluator.*;
import com.jintakkim.postevaluator.config.DbConfig;
import com.jintakkim.postevaluator.config.properties.DefinitionProperties;
import com.jintakkim.postevaluator.test.Executable;
import com.jintakkim.postevaluator.test.fixture.DefinitionFixture;
import com.jintakkim.postevaluator.test.fixture.FeatureDefinitionFixture;
import lombok.extern.slf4j.Slf4j;
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
    static final UserDefinition USED_USER_DEFINITION =  DefinitionFixture.USER_DEFINITION_1;
    static final PostDefinition USED_POST_DEFINITION =  DefinitionFixture.POST_DEFINITION_1;
    static final Map<String, Object> USED_USER_FEATURES = Map.of(
            FeatureDefinitionFixture.User.DISLIKE_TOPIC.name(), "study, coding",
            FeatureDefinitionFixture.User.LIKE_TOPIC.name(), "gaming"
    );
    static final Map<String, Object> USED_POST_FEATURES = Map.of(
            FeatureDefinitionFixture.Post.COMMENT_COUNT.name(), 100,
            FeatureDefinitionFixture.Post.CONTENT.name(), "아무 의미 없는 글"
    );

    static DbConfig dbConfig;
    static UserRepository userRepository;
    static PostRepository postRepository;
    static LabelRepository labelRepository;
    static SampleRepository sampleRepository;
    static JdbiContext jdbiContext;

    @BeforeAll
    static void initialize() {
        log.info("initialize test schema");
        dbConfig = new DbConfig("test.db", new DefinitionProperties(USED_USER_DEFINITION, USED_POST_DEFINITION));
        jdbiContext = dbConfig.jdbiContext;
        dbConfig.initialize();
        userRepository = dbConfig.userRepository;
        labelRepository = dbConfig.labelRepository;
        postRepository = dbConfig.postRepository;
        sampleRepository = dbConfig.sampleRepository;
    }

    @AfterAll
    static void cleanup() {
        log.info("test schema cleanup");
        dropAllTables();
    }

    static void rollbackTest(Executable executable) {
        jdbiContext.useTransaction(handle -> {
            executable.execute(jdbiContext);
            handle.rollback();
        });
    }

    static Post createPost(Long id) {
        return new Post(id, USED_POST_FEATURES);
    }

    static User createUser(Long id) {
        return new User(id, USED_USER_FEATURES);
    }

    static Label createLabel(Long postId, Long userId, double score) {
        return new Label(postId, userId, score, "mocked reason");
    }

    static void dropAllTables() {
        jdbiContext.useHandle(handle -> {
            handle.execute("DROP TABLE IF EXISTS label");
            handle.execute("DROP TABLE IF EXISTS post");
            handle.execute("DROP TABLE IF EXISTS user");
            handle.execute("DROP TABLE IF EXISTS table_metadata");
        });
    }

    static boolean tableExists(String tableName) {
        return jdbiContext.withHandle(handle ->
                handle.createQuery("SELECT name FROM sqlite_master WHERE type='table' AND name = :tableName")
                        .bind("tableName", tableName)
                        .mapTo(String.class)
                        .findFirst()
                        .isPresent()
        );
    }

    static boolean columnExists(String tableName, String columnName) {
        return jdbiContext.withHandle(handle -> {
            List<Map<String, Object>> columnsInfo = handle
                    .createQuery("PRAGMA table_info(" + tableName + ")")
                    .mapToMap()
                    .list();
            return columnsInfo.stream()
                    .anyMatch(row -> row.get("name").equals(columnName));
        });
    }
}
