package com.jintakkim.postevaluator.core.infra;

import com.jintakkim.postevaluator.core.LabeledPost;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LabeledPostMapper implements RowMapper<LabeledPost> {
    @Override
    public LabeledPost map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new LabeledPost(
                rs.getLong("id"),
                rs.getLong("post_id"),
                rs.getDouble("score"),
                rs.getString("reasoning")
        );
    }
}
