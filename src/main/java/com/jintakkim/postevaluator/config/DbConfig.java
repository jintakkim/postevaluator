package com.jintakkim.postevaluator.config;

import com.jintakkim.postevaluator.core.Label;
import com.jintakkim.postevaluator.core.Post;
import com.jintakkim.postevaluator.core.infra.*;
import com.jintakkim.postevaluator.feature.FeatureDefinitionProvider;
import org.jdbi.v3.core.Jdbi;

import java.nio.file.Paths;

public class DbConfig {
    private static final String DEFAULT_FILE_NAME = "post_evaluator.db";
    private static final String DIR = "user.home";

    public final Jdbi jdbi;
    public final LabeledPostRepository labeledPostRepository;
    public final PostRepository postRepository;

    private final PostDatabaseSynchronizer postTableSynchronizer;

    public DbConfig(String fileName, FeatureDefinitionProvider featureDefinitionProvider) {
        this.jdbi = Jdbi.create(createDbUrl(fileName));
        registerRowMappers();
        this.postTableSynchronizer = new PostDatabaseSynchronizer(jdbi, featureDefinitionProvider);
        postTableSynchronizer.synchronizeTable();
        this.postRepository = new JdbiPostRepository(jdbi, featureDefinitionProvider);
        this.labeledPostRepository = new JdbiLabeledPostRepository(jdbi);
    }

    public DbConfig(FeatureDefinitionProvider featureDefinitionProvider) {
        this(DEFAULT_FILE_NAME, featureDefinitionProvider);
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
}
