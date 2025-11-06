package com.jintakkim.postevaluator.core.infra;

import com.jintakkim.postevaluator.core.PostFeature;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostFeatureMapper implements RowMapper<PostFeature> {
    @Override
    public PostFeature map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new PostFeature(
                rs.getLong("id"),
                rs.getLong("view_count"),
                rs.getLong("like_count"),
                rs.getLong("dislike_count"),
                rs.getLong("comment_count"),
                rs.getString("content"),
                rs.getString("created_at")
        );
    }
}
