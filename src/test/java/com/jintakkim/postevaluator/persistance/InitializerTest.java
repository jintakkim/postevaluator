package com.jintakkim.postevaluator.persistance;

import com.jintakkim.postevaluator.persistance.initialization.InitStatus;
import com.jintakkim.postevaluator.persistance.initialization.LabelDatabaseInitializer;
import com.jintakkim.postevaluator.test.fixture.DefinitionFixture;
import com.jintakkim.postevaluator.persistance.initialization.TableDefinitionHashDatabaseInitializer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class InitializerTest extends LocalFileDbIntegrationTest {
    @Test
    @DisplayName("게시물 스키마가 없다면 새로 생성한다.")
    void initializeWhenPostSchemaIsAbsentShouldCreateNewTable() {
        assertCreateNewTable(() -> new TableDefinitionHashDatabaseInitializer(jdbiContext, "post", DefinitionFixture.POST_DEFINITION_1).initialize());
    }

    @Test
    @DisplayName("유저 스키마가 없다면 새로 생성한다.")
    void initializeWhenUserSchemaIsAbsentShouldCreateNewTable() {
        assertCreateNewTable(() -> new TableDefinitionHashDatabaseInitializer(jdbiContext, "user", DefinitionFixture.USER_DEFINITION_1).initialize());
    }

    @Test
    @DisplayName("라벨링 스키마가 없다면 생성한다.")
    void initializeWhenLabelSchemaIsAbsentShouldCreateNewTable() {
        assertCreateNewTable(() -> new LabelDatabaseInitializer(jdbiContext).initialize(InitStatus.NOT_CHANGED, InitStatus.NOT_CHANGED));
    }

    private void assertCreateNewTable(InitializerRunner initializerRunner) {
        rollbackTest((_) -> {
            dropAllTables();
            InitStatus initStatus = initializerRunner.run();
            Assertions.assertThat(initStatus).isEqualTo(InitStatus.NOT_EXISTS);
        });
    }

    @Test
    @DisplayName("게시물 스키마가 있지만 게시물의 특징이 변경되었더라면 게시물 스키마를 지우고 다시 생성한다.")
    void initializeWhenPostSchemaHaveChangedShouldRecreateTables() {
        assertRecreateTable(() -> new TableDefinitionHashDatabaseInitializer(jdbiContext, "post", DefinitionFixture.POST_DEFINITION_2).initialize());
    }

    @Test
    @DisplayName("유저 스키마가 있지만 게시물의 특징이 변경되었더라면 게시물 스키마를 지우고 다시 생성한다.")
    void initializeWhenUserSchemaHaveChangedShouldRecreateTables() {
        assertRecreateTable(() -> new TableDefinitionHashDatabaseInitializer(jdbiContext, "user", DefinitionFixture.USER_DEFINITION_2).initialize());
    }

    @ParameterizedTest
    @MethodSource("provideChangedStatuses")
    @DisplayName("라벨링 스키마가 있지만 라벨링의 기준이 되는 스키마가 변경되었더라면 라벨링 데이터를 삭제한다.")
    void shouldResetLabelingWhenBasisSchemaChanged(InitStatus postStatus, InitStatus userStatus) {
        assertRecreateTable(() -> new LabelDatabaseInitializer(jdbiContext).initialize(postStatus, userStatus));
    }

    private void assertRecreateTable(InitializerRunner initializerRunner) {
        rollbackTest((_) -> {
            InitStatus initStatus = initializerRunner.run();
            Assertions.assertThat(initStatus).isEqualTo(InitStatus.CHANGED);
        });
    }

    private static Stream<Arguments> provideChangedStatuses() {
        return Stream.of(
                Arguments.of(InitStatus.CHANGED,     InitStatus.NOT_CHANGED),
                Arguments.of(InitStatus.NOT_CHANGED, InitStatus.CHANGED),
                Arguments.of(InitStatus.CHANGED,     InitStatus.CHANGED)
        );
    }

    @Test
    @DisplayName("라벨링의 기준이 되는 스키마가 변경되지 않았더라면 아무 행동도 하지 않는다.")
    void shouldSkipInitializationWhenAllSchemasAreUpToDate() {
        assertDoNoting(() -> new LabelDatabaseInitializer(jdbiContext).initialize(InitStatus.NOT_CHANGED, InitStatus.NOT_CHANGED));
    }

    @Test
    @DisplayName("유저 스키마가 변경되지 않았더라면 아무 행동도 하지 않는다.")
    void shouldDoNothingWhenUserSchemaIsUnchanged() {
        assertDoNoting(() -> new TableDefinitionHashDatabaseInitializer(jdbiContext, "user", DefinitionFixture.USER_DEFINITION_1).initialize());
    }

    @Test
    @DisplayName("게시물 스키마가 변경되지 않았더라면 아무 행동도 하지 않는다.")
    void shouldDoNothingWhenPostSchemaIsUnchanged() {
        assertDoNoting(() -> new TableDefinitionHashDatabaseInitializer(jdbiContext, "post", DefinitionFixture.POST_DEFINITION_1
        ).initialize());
    }

    private void assertDoNoting(InitializerRunner initializerRunner) {
        rollbackTest((_) -> {
            InitStatus initStatus = initializerRunner.run();
            Assertions.assertThat(initStatus).isEqualTo(InitStatus.NOT_CHANGED);
        });
    }

    @FunctionalInterface
    interface InitializerRunner {
        InitStatus run();
    }
}
