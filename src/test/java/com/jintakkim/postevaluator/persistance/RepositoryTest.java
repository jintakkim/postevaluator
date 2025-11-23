package com.jintakkim.postevaluator.persistance;

import com.jintakkim.postevaluator.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RepositoryTest extends LocalFileDbIntegrationTest {
    @Test
    @DisplayName("Post를 저장한 뒤, 반환된 ID로 다시 조회하면 동일한 객체를 반환한다.")
    void shouldSaveAndRetrievePost() {
        rollbackTest((_) -> {
            Post savedPost = postRepository.save(createPost(null));
            List<Post> retrievePosts = postRepository.findAll();
            Assertions.assertThat(retrievePosts)
                    .hasSize(1)
                    .first()
                    .isEqualTo(savedPost);
        });
    }

    @Test
    @DisplayName("Post를 여러개 저장한 뒤, 반환된 ID로 다시 조회하면 동일한 객체들을 반환한다.")
    void shouldSaveAllAndRetrievePosts() {
        rollbackTest((_) -> {
            List<Post> posts = List.of(createPost(null), createPost(null));
            postRepository.saveAll(posts);
            List<Post> retrievePosts = postRepository.findAll();
            assertElementsEqualExcludeId(posts, retrievePosts);
        });
    }

    @Test
    @DisplayName("User를 저장한 뒤, 반환된 ID로 다시 조회하면 동일한 객체를 반환한다.")
    void shouldSaveAndRetrieveUser() {
        rollbackTest((_) -> {
            User savedUser = userRepository.save(createUser(null));
            List<User> retrieveUsers = userRepository.findAll();
            Assertions.assertThat(retrieveUsers)
                    .hasSize(1)
                    .first()
                    .isEqualTo(savedUser);
        });
    }

    @Test
    @DisplayName("User를 여러개 저장한 뒤, 반환된 ID로 다시 조회하면 동일한 객체들을 반환한다.")
    void shouldSaveAllAndRetrieveUsers() {
        rollbackTest((_) -> {
            List<User> users = List.of(createUser(null), createUser(null));
            userRepository.saveAll(users);
            List<User> retrieveUsers = userRepository.findAll();
            assertElementsEqualExcludeId(users, retrieveUsers);
        });
    }

    @Test
    @DisplayName("Label를 저장한 뒤, 반환된 ID로 다시 조회하면 동일한 객체를 반환한다.")
    void shouldSaveAndRetrieveLabel() {
        rollbackTest((_) -> {
            Post post = postRepository.save(createPost(null));
            User user = userRepository.save(createUser(null));
            Label retrieveLabel = labelRepository.save(createLabel(post.id(), user.id(), 1.0));
            Assertions.assertThat(labelRepository.findAll())
                    .hasSize(1)
                    .containsExactly(retrieveLabel);
        });
    }

    @Test
    @DisplayName("Label를 여러개 저장한 뒤, 반환된 ID로 다시 조회하면 동일한 객체들을 반환한다.")
    void shouldSaveAllAndRetrieveLabels() {
        rollbackTest((_) -> {
            Post post_1 = postRepository.save(createPost(null));
            Post post_2 = postRepository.save(createPost(null));
            User user = userRepository.save(createUser(null));

            List<Label> labels = List.of(
                    createLabel(post_1.id(), user.id(), 1.0),
                    createLabel(post_2.id(), user.id(), 0.5)
            );
            labelRepository.saveAll(labels);
            List<Label> retrieveLabels = labelRepository.findAll();
            assertElementsEqualExcludeId(labels, retrieveLabels);
        });
    }

    private <T> void assertElementsEqualExcludeId(List<T> expected, List<T> actual) {
        Assertions.assertThat(actual)
                .hasSize(expected.size())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @DisplayName("라벨링되지 않은 샘플을 리턴한다.")
    void shouldReturnUnlabeledSamplesExcludingLabeled() {
        rollbackTest((_) -> {
            Post post_1 = postRepository.save(createPost(null));
            Post post_2 = postRepository.save(createPost(null));
            User user = userRepository.save(createUser(null));
            labelRepository.save(createLabel(post_1.id(), user.id(), 1.0));
            List<UnlabeledSample> unlabeledSamples = sampleRepository.findUnlabeledSamples();

            Assertions.assertThat(unlabeledSamples)
                    .hasSize(1)
                    .first()
                    .isEqualTo(new UnlabeledSample(user, post_2));
        });
    }

    @Test
    @DisplayName("라벨링된 샘플을 리턴한다.")
    void shouldReturnLabeledSamples() {
        rollbackTest((_) -> {
            Post post_1 = postRepository.save(createPost(null));
            User user = userRepository.save(createUser(null));
            Label label = labelRepository.save(createLabel(post_1.id(), user.id(), 1.0));

            List<LabeledSample> labeledSamples = sampleRepository.findLabeledSamples();
            Assertions.assertThat(labeledSamples)
                    .hasSize(1)
                    .first()
                    .isEqualTo(new LabeledSample(user, post_1, label.score(), label.reasoning()));
        });
    }
}
