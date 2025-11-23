package com.jintakkim.postevaluator.persistance.mapping;

import com.jintakkim.postevaluator.EntitySchema;
import com.jintakkim.postevaluator.Post;
import com.jintakkim.postevaluator.UnlabeledSample;
import com.jintakkim.postevaluator.User;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class UnlabeledSampleMapper implements RowMapper<UnlabeledSample> {
    private final EntitySchema userSchema;
    private final EntitySchema postSchema;

    @Override
    public UnlabeledSample map(ResultSet rs, StatementContext ctx) throws SQLException {
        long userId = rs.getLong("u_id");
        Map<String, Object> userFields = MapperUtils.extractFieldsByAlias(rs, "u", Set.of("id"), userSchema);
        User user = new User(userId, userFields);

        long postId = rs.getLong("p_id");
        Map<String, Object> postFields = MapperUtils.extractFieldsByAlias(rs, "p", Set.of("id"), postSchema);
        Post post = new Post(postId, postFields);

        return new UnlabeledSample(user, post);
    }
}
