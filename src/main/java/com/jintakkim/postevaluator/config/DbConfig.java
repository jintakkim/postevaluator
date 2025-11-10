package com.jintakkim.postevaluator.config;

import com.jintakkim.postevaluator.core.LabeledPost;
import com.jintakkim.postevaluator.core.PostFeature;
import com.jintakkim.postevaluator.core.infra.*;
import org.jdbi.v3.core.Jdbi;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class DbConfig {
    private static final String DEFAULT_FILE_NAME = "post_evaluator.db";
    private static final String DIR = "user.home";
    private static final String SCHEMA_FILE = "schema.sql";

    public final Jdbi jdbi;
    public final LabeledPostRepository labeledPostRepository;
    public final PostFeatureRepository postFeatureRepository;

    public DbConfig(String fileName) {
        Jdbi jdbi = Jdbi.create(createDbUrl(fileName));
        jdbi.registerRowMapper(PostFeature.class, new PostFeatureMapper());
        jdbi.registerRowMapper(LabeledPost.class, new LabeledPostMapper());
        this.jdbi = jdbi;
        initializeDatabaseSchema();
        this.postFeatureRepository = new JdbiPostFeatureRepository(jdbi);
        this.labeledPostRepository = new JdbiLabeledPostRepository(jdbi);
    }

    public DbConfig() {
        this(DEFAULT_FILE_NAME);
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
