package com.jintakkim.postevaluator.persistance.initialization;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;

@RequiredArgsConstructor
@Slf4j
public class LabelDatabaseInitializer {
    private static final String LABEL_TABLE_CREATION_SQL = """
            CREATE TABLE label (
                id          INTEGER PRIMARY KEY,
                post_id     INTEGER NOT NULL,
                user_id     INTEGER NOT NULL,
                score       REAL NOT NULL,
                reasoning   TEXT NOT NULL,
        
                FOREIGN KEY(post_id) REFERENCES post(id) ON DELETE CASCADE,
                FOREIGN KEY(user_id) REFERENCES user(id) ON DELETE CASCADE
            );
            """;
    private final Jdbi jdbi;

    public InitStatus initialize(InitStatus postInitStatus, InitStatus userInitStatus) {
        if(!isTableExists()) {
            log.info("라벨링 테이블이 존재 하지 않기 떄문에 생성합니다.");
            createTable();
            return InitStatus.NOT_EXISTS;
        }

        if(hasChanged(postInitStatus, userInitStatus)) {
            deleteAll();
            log.info("post 훅은 user 테이블 구성이 변경되어 라벨링 정보를 삭제합니다.");
            return InitStatus.CHANGED;
        }
        return InitStatus.NOT_CHANGED;
    }

    private boolean isTableExists() {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT 1 FROM sqlite_master WHERE type='table' AND name = :tableName")
                .bind("tableName", "label")
                .mapTo(Integer.class)
                .findFirst()
                .isPresent());
    }

    private void createTable() {
        jdbi.useHandle(handle -> handle.execute(LABEL_TABLE_CREATION_SQL));
    }

    private boolean hasChanged(InitStatus postInitStatus, InitStatus userInitStatus) {
        return postInitStatus != InitStatus.NOT_CHANGED || userInitStatus != InitStatus.NOT_CHANGED;
    }

    private void deleteAll() {
        jdbi.useHandle(handle -> handle.execute("delete from label"));
        log.debug("라벨링 정보들을 전부 삭제 했습니다.");
    }
}
