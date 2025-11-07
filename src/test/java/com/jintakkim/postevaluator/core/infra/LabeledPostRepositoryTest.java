package com.jintakkim.postevaluator.core.infra;

import com.jintakkim.postevaluator.core.LabeledPost;
import com.jintakkim.postevaluator.core.PostFeature;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

public class LabeledPostRepositoryTest extends LocalFileDbIntegrationTest {
    @Test
    @DisplayName("LabeledPost 저장 후, findAll()로 조회 시 정확히 일치해야 한다")
    void saveShouldBeRetrievedByFindAll() {
        PostFeature postFeature = postFeatureRepository.save(createPostFeature());
        LabeledPost savedLabeledPost = labeledPostRepository.save(createLabeledPost(postFeature.id()));
        Assertions.assertThat(labeledPostRepository.findAll())
                .hasSize(1)
                .containsExactly(savedLabeledPost);
    }

    @Test
    @DisplayName("래이블링 되지 않은 featureId를 반환한다")
    void findUnlabeledFeatureIdsShouldReturnOnlyIdsWithNoLabels() {
        PostFeature postFeature_1 = postFeatureRepository.save(createPostFeature());
        PostFeature postFeature_2 = postFeatureRepository.save(createPostFeature());
        PostFeature postFeature_3 = postFeatureRepository.save(createPostFeature());

        labeledPostRepository.save(createLabeledPost(postFeature_1.id()));

        List<Long> unlabeledPost =labeledPostRepository.findUnlabeledFeatureIds();
        Assertions.assertThat(unlabeledPost)
                .hasSize(2)
                .containsExactlyInAnyOrder(postFeature_2.id(), postFeature_3.id());
    }

    private PostFeature createPostFeature() {
        return new PostFeature(
                0L,
                0L,
                0L,
                0L,
                "content",
                LocalDateTime.of(2025, 11, 7, 0,0).toString()
        );
    }

    private LabeledPost createLabeledPost(Long featureId) {
        return new LabeledPost(featureId, 1.0, "good", "manuel");
    }
}
