package com.jintakkim.postevaluator.core.infra;

import com.jintakkim.postevaluator.core.Label;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LabelMapper implements RowMapper<Label> {
    @Override
    public Label map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Label(
                rs.getLong("id"),
                rs.getLong("post_id"),
                rs.getDouble("score"),
                rs.getString("reasoning")
        );
    }
}
