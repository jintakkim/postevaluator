package com.jintakkim.postevaluator.persistance;

import com.jintakkim.postevaluator.Post;
import com.jintakkim.postevaluator.LabeledSample;
import com.jintakkim.postevaluator.User;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

public class LabeledSampleMapper implements RowMapper<LabeledSample> {
    @Override
    public LabeledSample map(ResultSet rs, StatementContext ctx) throws SQLException {
        double score = rs.getDouble("l_score");
        String reason = rs.getString("l_reason");

        long userId = rs.getLong("u_id");
        Map<String, Object> userFields = MapperUtils.extractFieldsByAlias(rs, "u", Set.of("id"));
        User user = new User(userId, userFields);

        long postId = rs.getLong("p_id");
        Map<String, Object> postFields = MapperUtils.extractFieldsByAlias(rs, "p", Set.of("id"));
        Post post = new Post(postId, postFields);

        return new LabeledSample(user, post, score, reason);
    }
}
