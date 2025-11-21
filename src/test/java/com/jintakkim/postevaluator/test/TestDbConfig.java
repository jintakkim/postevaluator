package com.jintakkim.postevaluator.test;

import com.jintakkim.postevaluator.core.Label;
import com.jintakkim.postevaluator.core.Post;
import com.jintakkim.postevaluator.core.infra.*;
import com.jintakkim.postevaluator.feature.FeatureDefinitionProvider;
import org.jdbi.v3.core.Jdbi;

import java.nio.file.Paths;

public class TestDbConfig {
    public static final String DEFAULT_FILE_NAME = "test.db";
    public static final String DIR = "user.home";

    public final Jdbi jdbi;
    public final LabeledPostRepository labeledPostRepository;
    public final PostRepository postRepository;
    public final PostDatabaseSynchronizer postTableSynchronizer;

    public TestDbConfig(FeatureDefinitionProvider featureDefinitionProvider) {
        this.jdbi = Jdbi.create(createDbUrl(DEFAULT_FILE_NAME));
        registerRowMappers();
        this.postTableSynchronizer = new PostDatabaseSynchronizer(jdbi, featureDefinitionProvider);
        this.postRepository = new JdbiPostRepository(jdbi, featureDefinitionProvider);
        this.labeledPostRepository = new JdbiLabeledPostRepository(jdbi);
    }

    public static TestDbConfig withSetting(FeatureDefinitionProvider FeatureDefinitionProvider, boolean synchronizeTable) {
        TestDbConfig testDbConfig = new TestDbConfig(FeatureDefinitionProvider);
        if(synchronizeTable) {
            testDbConfig.postTableSynchronizer.synchronizeTable();
        }
        return testDbConfig;
    }

    private void registerRowMappers() {
        jdbi.registerRowMapper(Post.class, new PostMapper());
        jdbi.registerRowMapper(Label.class, new LabelMapper());
    }

    private static String createDbUrl(String fileName) {
        if(fileName == null || !fileName.endsWith(".db"))
            throw new IllegalArgumentException("파일 명은 .db로 끝나야 합니다.");
        String dir = System.getProperty(DIR);
        return "jdbc:sqlite:" + Paths.get(dir, fileName);
    }

    public void cleanupTables() {
        jdbi.useHandle(handle -> {
            handle.execute("DROP TABLE IF EXISTS labeled_post");
            handle.execute("DROP TABLE IF EXISTS post");
            handle.execute("DROP TABLE IF EXISTS table_metadata");
        });
    }
}
