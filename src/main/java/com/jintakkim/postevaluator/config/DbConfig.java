package com.jintakkim.postevaluator.config;

import com.jintakkim.postevaluator.core.LabeledPost;
import com.jintakkim.postevaluator.core.Post;
import com.jintakkim.postevaluator.core.infra.*;
import com.jintakkim.postevaluator.feature.FeatureProvider;
import org.jdbi.v3.core.Jdbi;

import java.nio.file.Paths;

public class DbConfig {
    private static final String DEFAULT_FILE_NAME = "post_evaluator.db";
    private static final String DIR = "user.home";

    public final Jdbi jdbi;
    public final LabeledPostRepository labeledPostRepository;
    public final PostRepository postRepository;

    private final PostTableSynchronizer postTableSynchronizer;

    public DbConfig(String fileName, FeatureProvider featureProvider) {
        this.jdbi = Jdbi.create(createDbUrl(fileName));
        registerRowMappers();
        this.postTableSynchronizer = new PostTableSynchronizer(jdbi, featureProvider);
        postTableSynchronizer.synchronizeTable();
        this.postRepository = new JdbiPostRepository(jdbi, featureProvider);
        this.labeledPostRepository = new JdbiLabeledPostRepository(jdbi);
    }

    public DbConfig(FeatureProvider featureProvider) {
        this(DEFAULT_FILE_NAME, featureProvider);
    }

    private void registerRowMappers() {
        jdbi.registerRowMapper(Post.class, new PostMapper());
        jdbi.registerRowMapper(LabeledPost.class, new LabeledPostMapper());
    }

    /**
     * todo: 커스텀 환경 변수로으로 저장 디렉토리 위치 받을 수 있게 변경
     */
    private static String createDbUrl(String fileName) {
        if(fileName == null || !fileName.endsWith(".db"))
            throw new IllegalArgumentException("파일 명은 .db로 끝나야 합니다.");
        String dir = System.getProperty(DIR);
        return "jdbc:sqlite:" + Paths.get(dir, DEFAULT_FILE_NAME);
    }
}
