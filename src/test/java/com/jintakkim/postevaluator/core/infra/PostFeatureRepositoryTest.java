package com.jintakkim.postevaluator.core.infra;

import com.jintakkim.postevaluator.core.PostFeature;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

public class PostFeatureRepositoryTest extends LocalFileDbIntegrationTest {
    @Test
    @DisplayName("PostFeature를 저장한 뒤, 반환된 ID로 다시 조회하면 동일한 객체를 반환한다")
    void shouldSaveAndRetrievePostFeature() {
        PostFeature savedPostFeature = postFeatureRepository.save(createPostFeature());
        List<PostFeature> postFeatures = postFeatureRepository.findByIdIn(List.of(savedPostFeature.id()));
        Assertions.assertThat(postFeatures)
                .hasSize(1)
                .containsExactly(savedPostFeature);
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
}
