package com.jintakkim.postevaluator.core.persistance.initialization;

import com.jintakkim.postevaluator.core.EntitySchema;
import com.jintakkim.postevaluator.core.persistance.DynamicSqlGenerateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.util.Optional;

/**
 * 스키마의 구성 변경을 DefinitionHash를 기준으로 감지한다.
 * 만약 구성이 변경되었다면 테이블을 drop 후 새로 만든다.
 */
@Slf4j
@RequiredArgsConstructor
public class TableDefinitionHashDatabaseInitializer {
    private final Jdbi jdbi;
    private final String tableName;
    private final EntitySchema entitySchema;

    public InitStatus initialize() {
        String currentHash = entitySchema.getDefinitionHash();
        InitStatus initStatus = getStatus(currentHash);
        return switch (initStatus) {
            case NOT_EXISTS -> {
                handleNotChanged(currentHash);
                yield initStatus;
            }
            case CHANGED -> {
                handleChanged(currentHash);
                yield initStatus;
            }
            case NOT_CHANGED -> {
                handleNotExists(currentHash);
                yield initStatus;
            }
        };
    }

    private void handleNotChanged(String currentHash) {
        log.info("구성이 변경되지 않았습니다. 기존 스키마를 재활용합니다.");
    }

    private void handleChanged(String currentHash) {
        log.info("구성 변경이 감지되었습니다. 테이블을 재생성합니다");
        recreateTable(currentHash);
    }

    private void handleNotExists(String currentHash) {
        log.info("관련 스키마가 존재하지 않습니다. 테이블을 재생성합니다");
        recreateTable(currentHash);
    }

    private void recreateTable(String currentHash) {
        jdbi.useTransaction(handle -> {
            dropTable(handle);
            createTable(handle);
            updateTableHash(currentHash);
        });
    }

    private InitStatus getStatus(String currentHash) {
        Optional<String> optPastHash = findSavedHash(tableName);
        if (optPastHash.isEmpty()) return InitStatus.NOT_EXISTS;
        String pastHash = optPastHash.get();
        if(pastHash.equals(currentHash)) return InitStatus.NOT_CHANGED;
        return InitStatus.CHANGED;
    }

    private Optional<String> findSavedHash(String tableName) {
        createMetadataTableIfNotExists();
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT table_definition_hash FROM table_metadata WHERE table_name = :name")
                    .bind("name", tableName)
                    .mapTo(String.class)
                    .findFirst()
        );
    }

    private void dropTable(Handle handle) {
        handle.execute("DROP TABLE IF EXISTS " + tableName);
    }

    private void createTable(Handle handle) {
        String postTable = DynamicSqlGenerateUtils.generateTable(tableName, entitySchema.getColumnDefinitions());
        handle.execute(postTable);
    }

    private void createMetadataTableIfNotExists() {
        jdbi.useHandle(handle ->
                handle.execute("""
                        CREATE TABLE IF NOT EXISTS table_metadata
                            (table_name TEXT PRIMARY KEY, table_definition_hash TEXT NOT NULL)
                        """)
        );
    }

    private void updateTableHash(String newHash) {
        jdbi.useHandle(handle ->
                handle.createUpdate("INSERT OR REPLACE INTO table_metadata (table_name, table_definition_hash) VALUES (:name, :hash)")
                        .bind("name", tableName)
                        .bind("hash", newHash)
                        .execute()
        );
    }
}
