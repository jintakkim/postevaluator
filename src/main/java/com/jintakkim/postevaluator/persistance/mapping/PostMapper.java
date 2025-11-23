package com.jintakkim.postevaluator.persistance.mapping;

import com.jintakkim.postevaluator.EntitySchema;
import com.jintakkim.postevaluator.Post;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class PostMapper implements RowMapper<Post> {
    private static final String ID_COLUMN_NAME = "id";
    private final EntitySchema postSchema;

    @Override
    public Post map(ResultSet rs, StatementContext ctx) throws SQLException {
        Map<String, Object> fields = MapperUtils.extractFields(rs, Set.of(ID_COLUMN_NAME), postSchema);
        long id = rs.getLong(ID_COLUMN_NAME);
        return new Post(id, fields);
    }
}
