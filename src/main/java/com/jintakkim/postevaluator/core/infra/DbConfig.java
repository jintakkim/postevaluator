package com.jintakkim.postevaluator.core.infra;

import com.jintakkim.postevaluator.core.LabeledPost;
import com.jintakkim.postevaluator.core.PostFeature;
import org.jdbi.v3.core.Jdbi;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class DbConfig {
    private static final String DB_FILE = "post_evaluator.db";
    private static final String DIR = "user.home";
    private static final String SCHEMA_FILE = "schema.sql";

    public final Jdbi jdbi;
    public final LabeledPostRepository labeledPostRepository;
    public final PostFeatureRepository postFeatureRepository;

    public DbConfig() {
        Jdbi jdbi = Jdbi.create(createDbUrl());
        jdbi.registerRowMapper(PostFeature.class, new PostFeatureMapper());
        jdbi.registerRowMapper(LabeledPost.class, new LabeledPostMapper());
        this.jdbi = jdbi;
        initializeDatabaseSchema();
        this.postFeatureRepository = new LocalFilePostFeatureRepository(jdbi);
        this.labeledPostRepository = new LocalFileLabeledPostRepository(jdbi);
    }

    /**
     * todo: 커스텀 환경 변수로으로 저장 위치 받을 수 있게 변경
     */
    private static String createDbUrl() {
        String dir = System.getProperty(DIR);
        return "jdbc:sqlite:" + Paths.get(dir + DB_FILE);
    }

    private void initializeDatabaseSchema() {
        String schema = loadSqlFromResources(SCHEMA_FILE);
        jdbi.useHandle(handle -> handle.createScript(schema).execute());
    }

    private String loadSqlFromResources(String fileName) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) throw new IOException("sql 리소스를 찾을 수 없습니다: " + fileName);
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
