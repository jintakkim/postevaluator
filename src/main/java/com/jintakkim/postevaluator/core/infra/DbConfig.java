package com.jintakkim.postevaluator.core.infra;

import com.jintakkim.postevaluator.core.LabeledPost;
import com.jintakkim.postevaluator.core.PostFeature;
import org.jdbi.v3.core.Jdbi;

import java.nio.file.Paths;

public class DbConfig {
    private final Jdbi jdbi;

    public final LabeledPostRepository labeledPostRepository;
    public final PostFeatureRepository postFeatureRepository;

    public DbConfig() {
        Jdbi jdbi = Jdbi.create(createDbUrl());
        jdbi.registerRowMapper(PostFeature.class, new PostFeatureMapper());
        jdbi.registerRowMapper(LabeledPost.class, new LabeledPostMapper());
        this.jdbi = jdbi;
        this.postFeatureRepository = new LocalFilePostFeatureRepository(jdbi);
        this.labeledPostRepository = new LocalFileLabeledPostRepository(jdbi);
    }

    /**
     * home 위치에 저장
     * todo: 커스텀 환경 변수로으로 저장 위치 받을 수 있게 변경
     */
    private static String createDbUrl() {
        String homeDir = System.getProperty("user.home");
        return "jdbc:sqlite:" + Paths.get(homeDir + "post_evaluator.db");
    }
}
