package com.jintakkim.postevaluator.persistance;

import com.jintakkim.postevaluator.EntitySchema;
import com.jintakkim.postevaluator.Post;
import com.jintakkim.postevaluator.LabeledSample;
import com.jintakkim.postevaluator.User;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class JdbiLabeledSampleRepository implements LabeledSampleRepository {
    private final EntitySchema userSchema;
    private final EntitySchema postSchema;
    private final Jdbi jdbi;

    @Override
    public List<LabeledSample> findAll(int offset, int limit) {
        String sql = """
            SELECT
                %s, %s
                l.score AS l_score,
                l.reasoning AS l_reason
            FROM labels l
            INNER JOIN user u ON l.user_id = u.id
            INNER JOIN post p ON l.post_id = p.id
            ORDER BY l.id ASC
            LIMIT :limit OFFSET :offset
            """.formatted(
                DynamicSqlGenerateUtils.generateAlias("u", userSchema.getColumnDefinitions()),
                DynamicSqlGenerateUtils.generateAlias("p", postSchema.getColumnDefinitions())
        );

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("limit", limit)
                        .bind("offset", offset)
                        .map((rs, ctx) -> {
                            // 1. Label 데이터 추출
                            double score = rs.getDouble("l_score");
                            String reason = rs.getString("l_reason");
                            Map<String, Object> userFields = MapperUtils.extractFieldsByAlias(rs, "u", Set.of("id"));
                            Map<String, Object> postFields = MapperUtils.extractFieldsByAlias(rs, "p", Set.of("id"));
                            User user = new User(rs.getLong("u_id"), userFields);
                            Post post = new Post(rs.getLong("p_id"), postFields);
                            return new LabeledSample(user, post, score, reason);
                        })
                        .list()
        );
    }
}
