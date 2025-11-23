package com.jintakkim.postevaluator.persistance;

import com.jintakkim.postevaluator.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class JdbiSampleRepository implements SampleRepository {
    private final EntitySchema userSchema;
    private final EntitySchema postSchema;
    private final JdbiContext jdbiContext;

    @Override
    public List<LabeledSample> findLabeledSamples(int offset, int limit) {
        String sql = buildLabeledSelectSql(true);
        return jdbiContext.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("limit", limit)
                        .bind("offset", offset)
                        .mapTo(LabeledSample.class)
                        .list()
        );
    }

    @Override
    public List<LabeledSample> findLabeledSamples() {
        String sql = buildLabeledSelectSql(false);
        return jdbiContext.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(LabeledSample.class)
                        .list()
        );
    }

    @Override
    public List<UnlabeledSample> findUnlabeledSamples(int offset, int limit) {
        String sql = buildUnlabeledSelectSql(true);
        return jdbiContext.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("limit", limit)
                        .bind("offset", offset)
                        .mapTo(UnlabeledSample.class)
                        .list()
        );
    }

    @Override
    public List<UnlabeledSample> findUnlabeledSamples() {
        String sql = buildUnlabeledSelectSql(false);
        return jdbiContext.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(UnlabeledSample.class)
                        .list()
        );
    }

    private String buildLabeledSelectSql(boolean pagination) {
        String userSelect = DynamicSqlGenerateUtils.generateAlias("u", userSchema.getColumnDefinitions());
        String postSelect = DynamicSqlGenerateUtils.generateAlias("p", postSchema.getColumnDefinitions());

        String baseSql = """
            SELECT
                %s,
                %s,
                l.score AS l_score,
                l.reasoning AS l_reason
            FROM label l
            INNER JOIN user u ON l.user_id = u.id
            INNER JOIN post p ON l.post_id = p.id
            ORDER BY l.id ASC
            """.formatted(userSelect, postSelect);

        return pagination ? baseSql + " LIMIT :limit OFFSET :offset" : baseSql;
    }

    private String buildUnlabeledSelectSql(boolean pagination) {
        String userSelect = DynamicSqlGenerateUtils.generateAlias("u", userSchema.getColumnDefinitions());
        String postSelect = DynamicSqlGenerateUtils.generateAlias("p", postSchema.getColumnDefinitions());

        String baseSql = """
            SELECT
                %s,
                %s
            FROM user u
            CROSS JOIN post p
            LEFT JOIN label l ON u.id = l.user_id AND p.id = l.post_id
            WHERE l.id IS NULL
            ORDER BY u.id, p.id
            """.formatted(userSelect, postSelect);

        return pagination ? baseSql + " LIMIT :limit OFFSET :offset" : baseSql;
    }
}