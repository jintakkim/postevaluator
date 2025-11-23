package com.jintakkim.postevaluator.core.persistance;

import com.jintakkim.postevaluator.core.DynamicEntity;
import com.jintakkim.postevaluator.core.EntitySchema;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.PreparedBatch;
import org.jdbi.v3.core.statement.Update;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DynamicEntityRepositoryUtils {

    public static <T extends DynamicEntity> Long save(Jdbi jdbi, String tableName, EntitySchema entitySchema, T entity) {
        Map<String, String> fieldToColumnMap = entitySchema.getFieldToColumnMap();
        String sql = DynamicSqlGenerateUtils.generateInsert(tableName, fieldToColumnMap.values());
        return jdbi.withHandle(handle -> {
            Update updateStatement = handle.createUpdate(sql);
            bindEntityParameters(updateStatement, entity, fieldToColumnMap);
            return updateStatement.executeAndReturnGeneratedKeys("id")
                    .mapTo(Long.class)
                    .one();
        });
    }

    public static <T extends DynamicEntity> void batchSave(Jdbi jdbi, String tableName, EntitySchema entitySchema, Collection<T> entities) {
        Map<String, String> fieldToColumnMap = entitySchema.getFieldToColumnMap();
        String sql = DynamicSqlGenerateUtils.generateInsert(tableName, fieldToColumnMap.values());
        jdbi.useTransaction(handle -> {
            PreparedBatch batch = handle.prepareBatch(sql);
            for(T entity: entities) {
                bindBatchParameters(batch, entity, fieldToColumnMap);
                batch.add();
            }
            batch.execute();
        });
    }

    private static <T extends DynamicEntity> void bindEntityParameters(Update updateStatement, T entity, Map<String, String> fieldToColumnMap) {
        for (Map.Entry<String, String> entry : fieldToColumnMap.entrySet()) {
            String column = entry.getValue();
            updateStatement.bind(column, entity.get(entry.getKey()));
        }
    }

    private static <T extends DynamicEntity> void bindBatchParameters(PreparedBatch batch, T entity, Map<String, String> fieldToColumnMap) {
        for (Map.Entry<String, String> entry : fieldToColumnMap.entrySet()) {
            String field = entry.getKey();
            String column = entry.getValue();
            batch.bind(column, entity.get(field));
        }
    }
}
