package com.jintakkim.postevaluator.core.infra;

import com.jintakkim.postevaluator.feature.Feature;
import com.jintakkim.postevaluator.fixture.FeatureFixture;
import com.jintakkim.postevaluator.test.TestFeatureProviderGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DatabaseSynchronizerTest extends LocalFileDbIntegrationTest {

    private static final List<Feature> FEATURES_TO_CHANGE = List.of(
            FeatureFixture.VIEW_COUNT,
            FeatureFixture.LIKE_COUNT
    );

    @Test
    @DisplayName("게시물 스키마가 없다면 새로 생성한다.")
    void synchronizeWhenSchemaIsAbsentShouldCreateNewTables() {
        rollbackTest((handle) -> {
            cleanupTables();
            postTableSynchronizer.synchronizeTable();
            Assertions.assertThat(tableExists("post")).isTrue();
            Assertions.assertThat(tableExists("labeled_post")).isTrue();
        });
    }

    @Test
    @DisplayName("게시물 스키마가 있지만 게시물의 특징이 변경되었더라면 게시물 스키마를 지우고 다시 생성한다.")
    void synchronizeWhenFeaturesHaveChangedShouldRecreateTables() {
        rollbackTest((handle) -> {
            PostDatabaseSynchronizer updatedTableSynchronizer = new PostDatabaseSynchronizer(jdbi, TestFeatureProviderGenerator.generate(FEATURES_TO_CHANGE));
            updatedTableSynchronizer.synchronizeTable();
            Assertions.assertThat(tableExists("post")).isTrue();
            Assertions.assertThat(tableExists("labeled_post")).isTrue();
            Assertions.assertThat(columnExists("post", "view_count")).isTrue();
            Assertions.assertThat(columnExists("post", "like_count")).isTrue();
        });
    }
}
