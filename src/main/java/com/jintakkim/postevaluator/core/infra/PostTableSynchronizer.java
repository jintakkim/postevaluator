package com.jintakkim.postevaluator.core.infra;

import com.jintakkim.postevaluator.feature.Feature;
import com.jintakkim.postevaluator.feature.FeatureProvider;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PostTableSynchronizer extends TableSynchronizer {
    private static final String POST_TABLE_NAME = "post";
    private static final String LABELED_POST_TABLE_NAME = "labeled_post";

    private final FeatureProvider featureProvider;

    public PostTableSynchronizer(Jdbi jdbi, FeatureProvider featureProvider) {
        super(jdbi);
        this.featureProvider = featureProvider;
    }

    @Override
    protected String getTableName() {
        return POST_TABLE_NAME;
    }

    @Override
    protected String getCurrentTableHash() {
        List<Feature> features = featureProvider.getFeatures();
        if (features == null || features.isEmpty()) throw new IllegalArgumentException("features는 null이거나 empty일 수 없습니다.");
        String combinedHashes = features.stream()
                .map(Feature::getDefinitionHash)
                .sorted()
                .collect(Collectors.joining("|"));

        return UUID.nameUUIDFromBytes(combinedHashes.getBytes(StandardCharsets.UTF_8)).toString();
    }

    @Override
    protected void dropTable(Handle handle) {
        handle.execute("DROP TABLE IF EXISTS " + LABELED_POST_TABLE_NAME);
        handle.execute("DROP TABLE IF EXISTS " + POST_TABLE_NAME);
    }

    @Override
    protected void createTable(Handle handle) {
        String postTable = createPostTable();
        System.out.println(postTable);
        handle.execute(postTable);
        String labeledPostTable = createLabeledPostTable();
        handle.execute(labeledPostTable);
    }

    private String createPostTable() {
        List<ColumnDefinition> columnDefinitions = featureProvider.getFeatures().stream()
                .map(feature -> new ColumnDefinition(feature.getName(), SqlType.convertFeatureTypeToSqlType(feature.getType()), false))
                .toList();
        return DynamicTableGenerator.generate(POST_TABLE_NAME, columnDefinitions);
    }

    private String createLabeledPostTable() {
        return loadSqlFromResources("labeled-post-table.sql");
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
