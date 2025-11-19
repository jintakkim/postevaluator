package com.jintakkim.postevaluator.core.infra;

import com.google.common.base.CaseFormat;
import com.jintakkim.postevaluator.core.Post;
import com.jintakkim.postevaluator.feature.FeatureProvider;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Update;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JdbiPostRepository implements PostRepository {
    private final Jdbi jdbi;
    private final FeatureProvider featureProvider;

    @Override
    public Post save(Post post) {
        List<String> snakeCaseFeatureColumnNames = featureProvider.getFeatureNames().stream()
                .map(camelName-> CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, camelName))
                .toList();
        String sql = generateInsertSql(snakeCaseFeatureColumnNames);
        long generatedId = jdbi.withHandle(handle -> {
            Update updateStatement = handle.createUpdate(sql);
            for (String columnName : snakeCaseFeatureColumnNames) {
                Object value = post.features().get(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName));
                updateStatement.bind(columnName, value);
            }
            return updateStatement.executeAndReturnGeneratedKeys("id")
                    .mapTo(Long.class)
                    .one();
        });
        return new Post(generatedId, post.features());
    }

    private String generateInsertSql(List<String> snakeCaseFeatureColumnNames) {
        String columnsPart = String.join(", ", snakeCaseFeatureColumnNames);
        String valuesPart = snakeCaseFeatureColumnNames.stream()
                .map(col -> ":" + col)
                .collect(Collectors.joining(", "));

        return String.format("INSERT INTO post (%s) VALUES (%s)", columnsPart, valuesPart);
    }


    @Override
    public List<Post> findByIdIn(Collection<Long> ids) {
        if(ids == null || ids.isEmpty()) return List.of();
        String sql = "SELECT * FROM post WHERE id IN (<ids>)";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bindList("ids", ids)
                        .mapTo(Post.class)
                        .list()
        );
    }

    @Override
    public int count() {
        String sql = "SELECT COUNT(*) FROM post";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM post";
        jdbi.useHandle(handle ->
                handle.createUpdate(sql)
                        .execute()
        );
    }
}
