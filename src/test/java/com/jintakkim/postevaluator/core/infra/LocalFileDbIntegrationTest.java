package com.jintakkim.postevaluator.core.infra;

import com.jintakkim.postevaluator.config.DbConfig;
import com.jintakkim.postevaluator.core.LabeledPost;
import com.jintakkim.postevaluator.core.Post;
import com.jintakkim.postevaluator.feature.Feature;
import com.jintakkim.postevaluator.feature.FeatureProvider;
import com.jintakkim.postevaluator.fixture.FeatureFixture;
import lombok.extern.slf4j.Slf4j;
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

    protected static LabeledPostRepository labeledPostRepository;
    protected static PostRepository postRepository;
    protected static Jdbi jdbi;

    @BeforeAll
    static void initialize() {
        DbConfig dbConfig = new DbConfig("test.db", featureProvider());
        jdbi = dbConfig.jdbi;
        labeledPostRepository = dbConfig.labeledPostRepository;
        postRepository = dbConfig.postRepository;
    }

    static void rollbackTest(Runnable runnable) {
        jdbi.useTransaction(handle -> {
            runnable.run();
            handle.rollback();
        });
    }

    static FeatureProvider featureProvider() {
        return new FeatureProvider() {
            @Override
            public List<String> getFeatureNames() {
                return TEST_FEATURES.stream().map(Feature::getName).toList();
            }

            @Override
            public List<Feature> getFeatures() {
                return TEST_FEATURES;
            }
        };
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
}
