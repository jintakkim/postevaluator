package com.jintakkim.postevaluator.core.persistance;

import com.jintakkim.postevaluator.Post;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PostRepositoryTest extends LocalFileDbIntegrationTest {
    @Test
    @DisplayName("PostFeature를 저장한 뒤, 반환된 ID로 다시 조회하면 동일한 객체를 반환한다")
    void shouldSaveAndRetrievePostFeature() {
        rollbackTest((handle) -> {
            Post savedPost = postRepository.save(createPost());
            List<Post> posts = postRepository.findByIdIn(List.of(savedPost.id()));
            Assertions.assertThat(posts)
                    .hasSize(1)
                    .extracting(Post::id)
                    .contains(savedPost.id());
        });
    }
}
