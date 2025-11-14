package com.jintakkim.postevaluator.core.infra;

import com.jintakkim.postevaluator.config.DbConfig;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * 실제 로컬 파일에 저장이 되는 테스트
 */
@Slf4j
@Execution(ExecutionMode.SAME_THREAD) //기본값이지만 db 데이터에 의해 테스트간 격리가 깨질 수 있으므로 명시
public abstract class LocalFileDbIntegrationTest {
    protected static LabeledPostRepository labeledPostRepository;
    protected static PostFeatureRepository postFeatureRepository;
    protected static Jdbi jdbi;

    @BeforeAll
    static void initialize() {
        log.info("db 설정");
        DbConfig dbConfig = new DbConfig("test.db");
        jdbi = dbConfig.jdbi;
        labeledPostRepository = dbConfig.labeledPostRepository;
        postFeatureRepository = dbConfig.postFeatureRepository;
    }

    @AfterAll
    static void clearAll() {
        log.info("테이블의 모든 데이터 삭제");
        jdbi.useHandle(handle -> {
            handle.execute("DELETE FROM labeled_post");
            handle.execute("DELETE FROM post_feature");
            //id 시퀀스를 다시 처음 부터 시작
            handle.execute("DELETE FROM sqlite_sequence WHERE name IN ('labeled_post', 'post_feature')");
        });
    }
}
