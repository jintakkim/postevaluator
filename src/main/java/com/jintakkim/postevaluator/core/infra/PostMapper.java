package com.jintakkim.postevaluator.core.infra;

import com.google.common.base.CaseFormat;
import com.jintakkim.postevaluator.core.Post;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PostMapper implements RowMapper<Post> {
    private static final String ID_COLUMN_NAME = "id";

    @Override
    public Post map(ResultSet rs, StatementContext ctx) throws SQLException {
        Map<String, Object> features = extractFeatures(rs);
        long id = rs.getLong(ID_COLUMN_NAME);
        return new Post(id, features);
    }

    private Map<String, Object> extractFeatures(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        Map<String, Object> features = new HashMap<>();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, metaData.getColumnName(i));
            if (ID_COLUMN_NAME.equals(columnName)) continue;
            Object columnValue = rs.getObject(i);
            features.put(columnName, columnValue);
        }
        return features;
    }
}
