package com.jintakkim.postevaluator.core.persistance;

import com.jintakkim.postevaluator.Label;
import com.jintakkim.postevaluator.Post;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class LabelRepositoryTest extends LocalFileDbIntegrationTest {

    @Test
    @DisplayName("Label 저장 후, findAll()로 조회 시 정확히 일치해야 한다")
    void saveShouldBeRetrievedByFindAll() {
        rollbackTest((handle) -> {
            Post post = postRepository.save(createPost());
            Label savedLabel = labelRepository.save(createLabeledPost(post.id()));
            Assertions.assertThat(labelRepository.findAll())
                    .hasSize(1)
                    .containsExactly(savedLabel);
        });
    }

    @Test
    @DisplayName("래이블링 되지 않은 postId를 반환한다")
    void findUnlabeledFeatureIdsShouldReturnOnlyIdsWithNoLabels() {
        rollbackTest((handle) -> {
            Post post_1 = postRepository.save(createPost());
            Post post_2 = postRepository.save(createPost());
            Post post_3 = postRepository.save(createPost());

            labelRepository.save(createLabeledPost(post_1.id()));
            List<Long> unlabeledPost = labelRepository.findUnlabeledPostIds();
            Assertions.assertThat(unlabeledPost)
                    .hasSize(2)
                    .containsExactlyInAnyOrder(post_2.id(), post_3.id());
        });
    }
}
