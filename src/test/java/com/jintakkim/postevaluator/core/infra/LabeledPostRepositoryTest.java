package com.jintakkim.postevaluator.core.infra;

import com.jintakkim.postevaluator.core.LabeledPost;
import com.jintakkim.postevaluator.core.Post;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class LabeledPostRepositoryTest extends LocalFileDbIntegrationTest {

    @Test
    @DisplayName("LabeledPost 저장 후, findAll()로 조회 시 정확히 일치해야 한다")
    void saveShouldBeRetrievedByFindAll() {
        rollbackTest(() -> {
            Post post = postRepository.save(createPost());
            LabeledPost savedLabeledPost = labeledPostRepository.save(createLabeledPost(post.id()));
            Assertions.assertThat(labeledPostRepository.findAll())
                    .hasSize(1)
                    .containsExactly(savedLabeledPost);
        });
    }

    @Test
    @DisplayName("래이블링 되지 않은 postId를 반환한다")
    void findUnlabeledFeatureIdsShouldReturnOnlyIdsWithNoLabels() {
        rollbackTest(() -> {
            Post post_1 = postRepository.save(createPost());
            Post post_2 = postRepository.save(createPost());
            Post post_3 = postRepository.save(createPost());

            labeledPostRepository.save(createLabeledPost(post_1.id()));
            List<Long> unlabeledPost =labeledPostRepository.findUnlabeledPostIds();
            Assertions.assertThat(unlabeledPost)
                    .hasSize(2)
                    .containsExactlyInAnyOrder(post_2.id(), post_3.id());
        });
    }
}
