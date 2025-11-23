package com.jintakkim.postevaluator.persistance.mapping;

import com.jintakkim.postevaluator.User;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

public class UserMapper implements RowMapper<User> {
    private static final String ID_COLUMN_NAME = "id";

    @Override
    public User map(ResultSet rs, StatementContext ctx) throws SQLException {
        Map<String, Object> fields = MapperUtils.extractFields(rs, Set.of(ID_COLUMN_NAME));
        long id = rs.getLong(ID_COLUMN_NAME);
        return new User(id, fields);
    }
}
