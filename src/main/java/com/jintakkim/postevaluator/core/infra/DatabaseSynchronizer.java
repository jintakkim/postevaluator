package com.jintakkim.postevaluator.core.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.util.Optional;

/**
 * 스키마의 구성 변경을 감지
 * 만약 구성이 변경되었다면 테이블을 drop 후 새로 만든다.
 */
@Slf4j
@RequiredArgsConstructor
public abstract class DatabaseSynchronizer {
    private final Jdbi jdbi;

    public void synchronizeTable() {
        String currentHash = getCurrentTableHash();
        Status status = getStatus(currentHash);
        if (status == Status.NOT_CHANGED) {
            log.info("구성이 변경되지 않았습니다. 기존 스키마를 재활용합니다.");
            return;
        }
        if (status == Status.CHANGED) {
            handleChanged(currentHash);
            return;
        }
        if(status == Status.NOT_EXISTS) {
            handleNotExists(currentHash);
        }
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
            updateTableHash(getTableName(), currentHash);
        });
    }

    protected abstract String getTableName();

    protected abstract String getCurrentTableHash();

    private Status getStatus(String currentHash) {
        Optional<String> optPastHash = findSavedHash(getTableName());
        if (optPastHash.isEmpty()) return Status.NOT_EXISTS;
        String pastHash = optPastHash.get();
        if(pastHash.equals(currentHash)) return Status.NOT_CHANGED;
        return Status.CHANGED;
    }

    private enum Status {
        NOT_EXISTS, CHANGED, NOT_CHANGED
    }

    protected Optional<String> findSavedHash(String tableName) {
        createTableMetadataTableIfNotExists();
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT table_hash FROM table_metadata WHERE table_name = :name")
                    .bind("name", tableName)
                    .mapTo(String.class)
                    .findFirst()
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
