package com.jintakkim.postevaluator.core.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

/**
 * 스키마의 구성 변경을 감지
 * 만약 구성이 변경되었다면 테이블을 drop 후 새로 만든다.
 */
@Slf4j
@RequiredArgsConstructor
public abstract class TableSynchronizer {
    private final Jdbi jdbi;

    public void synchronizeTable() {
        String currentHash = getCurrentTableHash();
        if (isChanged(currentHash)) {
            log.info("구성 변경이 감지되었습니다. 테이블을 재생성합니다");
            jdbi.useTransaction(handle -> {
                dropTable(handle);
                createTable(handle);
                updateTableHash(getTableName(), currentHash);
            });
        } else {
            log.info("테이블이 최신 버전입니다. 기존 테이블을 재사용합니다.");
        }
    }

    protected abstract String getTableName();

    protected abstract String getCurrentTableHash();

    private boolean isChanged(String currentHash) {
        String pastHash = findSavedHash(getTableName());
        return !currentHash.equals(pastHash);
    }

    protected String findSavedHash(String tableName) {
        createTableMetadataTableIfNotExists();
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT table_hash FROM table_metadata WHERE table_name = :name")
                    .bind("name", tableName)
                    .mapTo(String.class)
                    .findFirst()
                    .orElse(null)
        );
    }

    protected abstract void dropTable(Handle handle);
    protected abstract void createTable(Handle handle);

    private void createTableMetadataTableIfNotExists() {
        jdbi.useHandle(handle ->
                handle.execute("""
                        CREATE TABLE IF NOT EXISTS table_metadata
                            (table_name TEXT PRIMARY KEY, table_hash TEXT NOT NULL)
                        """)
        );
    }

    private void updateTableHash(String tableName, String newHash) {
        jdbi.useHandle(handle ->
                handle.createUpdate("INSERT OR REPLACE INTO table_metadata (table_name, table_hash) VALUES (:name, :hash)")
                        .bind("name", tableName)
                        .bind("hash", newHash)
                        .execute()
        );
    }
}
