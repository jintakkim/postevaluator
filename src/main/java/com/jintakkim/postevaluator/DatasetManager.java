package com.jintakkim.postevaluator;

import com.jintakkim.postevaluator.core.LabeledPost;
import com.jintakkim.postevaluator.core.Post;
import com.jintakkim.postevaluator.core.SetupStrategy;
import com.jintakkim.postevaluator.core.infra.LabeledPostRepository;
import com.jintakkim.postevaluator.core.infra.PostRepository;
import com.jintakkim.postevaluator.evaluation.PostToEvaluation;
import com.jintakkim.postevaluator.generation.FeatureGenerator;
import com.jintakkim.postevaluator.labeling.Labeler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class DatasetManager {
    private final int targetDatasetSize;
    private final SetupStrategy setupStrategy;
    private final PostRepository postRepository;
    private final LabeledPostRepository labeledPostRepository;
    private final FeatureGenerator featureGenerator;
    private final Labeler labeler;

    public void initializeDataset() {
        switch (setupStrategy) {
            case CLEAR:
                handleClearStrategy();
                break;
            case REUSE_STRICT:
                handleReuseStrictStrategy();
                break;
            case REUSE_RANDOM:
                handleReuseRandomStrategy();
                break;
        }
    }

    private void handleClearStrategy() {
        postRepository.deleteAll();
        labeledPostRepository.deleteAll();
        generateAndSaveFeatures(targetDatasetSize);
        doLabeling();
    }

    private void handleReuseStrictStrategy() {
        int currentCount = postRepository.count();
        int countToMake = targetDatasetSize - currentCount;
        if (countToMake < 0) {
            throw new IllegalStateException("기존 데이터가 필요한 데이터보다 많습니다. SetupStrategy을 CLEAR 혹은 REUSE_RANDOM으로 변경하세요.");
        }
        generateAndSaveFeatures(countToMake);
        doLabeling();
    }

    private void handleReuseRandomStrategy() {
        int currentCount = postRepository.count();
        int countToMake = targetDatasetSize - currentCount;
        generateAndSaveFeatures(countToMake);
        doLabeling();
    }

    private void generateAndSaveFeatures(int count) {
        if (count <= 0) return;
        log.info("{}개의 데이터를 {} 모델을 통해 생성합니다.", count, featureGenerator.getModelName());
        featureGenerator.generate(count).forEach(postRepository::save);
    }

    private void doLabeling() {
        List<Long> unlabeledFeatureIds = labeledPostRepository.findUnlabeledPostIds();
        log.info("레이블링되지 않은 {}개의 데이터를 레이블링 합니다.", unlabeledFeatureIds.size());
        List<Post> posts = postRepository.findByIdIn(unlabeledFeatureIds);
        labeler.label(posts).forEach(labeledPostRepository::save);
    }

    public List<PostToEvaluation> getEvaluationPosts() {
        List<LabeledPost> labeledPosts = getLabeledPostsToEvaluate();
        Map<Long, Post> postFeatures = postRepository.findByIdIn(extractFeatureIds(labeledPosts)).stream()
                .collect(Collectors.toMap(Post::id, Function.identity()));
        return labeledPosts.stream()
                .map(labeledPost -> new PostToEvaluation(
                        postFeatures.get(labeledPost.postId()),
                        labeledPost.score(),
                        labeledPost.reasoning()
                        )
                ).toList();
    }

    private List<LabeledPost> getLabeledPostsToEvaluate() {
        if(labeledPostRepository.count() > targetDatasetSize)
            return labeledPostRepository.findRandomly(targetDatasetSize);
        return labeledPostRepository.findAll();
    }

    private List<Long> extractFeatureIds(List<LabeledPost> labeledPosts) {
        return labeledPosts.stream().map(LabeledPost::postId).toList();
    }
}
