package com.jintakkim.postevaluator.persistance.mapping;

import com.jintakkim.postevaluator.EntitySchema;
import com.jintakkim.postevaluator.Post;
import com.jintakkim.postevaluator.LabeledSample;
import com.jintakkim.postevaluator.User;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class LabeledSampleMapper implements RowMapper<LabeledSample> {
    private final EntitySchema userSchema;
    private final EntitySchema postSchema;

    @Override
    public LabeledSample map(ResultSet rs, StatementContext ctx) throws SQLException {
        double score = rs.getDouble("l_score");
        String reason = rs.getString("l_reason");

        long userId = rs.getLong("u_id");
        Map<String, Object> userFields = MapperUtils.extractFieldsByAlias(rs, "u", Set.of("id"), userSchema);
        User user = new User(userId, userFields);

        long postId = rs.getLong("p_id");
        Map<String, Object> postFields = MapperUtils.extractFieldsByAlias(rs, "p", Set.of("id"), postSchema);
        Post post = new Post(postId, postFields);

        return new LabeledSample(user, post, score, reason);
    }
}
