package com.jintakkim.postevaluator.core.persistance;

import com.google.common.base.CaseFormat;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MapperUtils {
    /**
     * @param excludeFields 제외하고 싶은 필드 명(camelCase)
     */
    public static Map<String, Object> extractFields(ResultSet rs, Set<String> excludeFields) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        Map<String, Object> fields = new HashMap<>();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = toCamelCase(metaData.getColumnLabel(i));
            if (excludeFields.contains(columnName)) continue;
            Object field = rs.getObject(i);
            fields.put(columnName, field);
        }
        return fields;
    }

    /**
     * @param alias 해당 alias를 가진 필드만 조회됨
     * @param excludeFields 제외하고 싶은 필드명(camelCase, alias 미포함) ex) db에서 p_like_count로 조회되었다면 likeCount로 입력
     */
    public static Map<String, Object> extractFieldsByAlias(ResultSet rs, String alias, Set<String> excludeFields) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int colCount = meta.getColumnCount();

        String prefix = alias + "_";
        Map<String, Object> fields = new HashMap<>();

        for (int i = 1; i <= colCount; i++) {
            String label = meta.getColumnLabel(i);
            if (label.startsWith(prefix)) {
                String snakeName = label.substring(prefix.length());
                String camelName = toCamelCase(snakeName);
                Object value = rs.getObject(i);
                if (excludeFields.contains(camelName)) continue;
                fields.put(camelName, value);
            }
        }
        return fields;
    }

    private static String toCamelCase(String snakeCase) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, snakeCase);
    }
}
