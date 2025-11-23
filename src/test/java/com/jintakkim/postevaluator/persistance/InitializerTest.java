package com.jintakkim.postevaluator.persistance;

import com.jintakkim.postevaluator.feature.FeatureDefinition;
import com.jintakkim.postevaluator.fixture.FeatureFixture;
import com.jintakkim.postevaluator.test.TestFeatureDefinitionProviderGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class InitializerTest extends LocalFileDbIntegrationTest {

    private static final List<FeatureDefinition> FEATURES_TO_CHANGE = List.of(
            FeatureFixture.VIEW_COUNT,
            FeatureFixture.LIKE_COUNT
    );

    @Test
    @DisplayName("게시물 스키마가 없다면 새로 생성한다.")
    void synchronizeWhenSchemaIsAbsentShouldCreateNewTables() {
        rollbackTest((handle) -> {
            cleanupTables();
            postTableSynchronizer.synchronize();
            Assertions.assertThat(tableExists("post")).isTrue();
            Assertions.assertThat(tableExists("labeled_post")).isTrue();
        });
    }

    @Test
    @DisplayName("게시물 스키마가 있지만 게시물의 특징이 변경되었더라면 게시물 스키마를 지우고 다시 생성한다.")
    void synchronizeWhenFeaturesHaveChangedShouldRecreateTables() {
        rollbackTest((handle) -> {
            PostDatabaseInitializer updatedTableSynchronizer = new PostDatabaseInitializer(jdbi, TestFeatureDefinitionProviderGenerator.generate(FEATURES_TO_CHANGE));
            updatedTableSynchronizer.synchronize();
            Assertions.assertThat(tableExists("post")).isTrue();
            Assertions.assertThat(tableExists("labeled_post")).isTrue();
            Assertions.assertThat(columnExists("post", "view_count")).isTrue();
            Assertions.assertThat(columnExists("post", "like_count")).isTrue();
        });
    }
}
