package com.jintakkim.postevaluator.persistance;

import com.jintakkim.postevaluator.*;
import com.jintakkim.postevaluator.generation.PostGenerator;
import com.jintakkim.postevaluator.generation.UserGenerator;
import com.jintakkim.postevaluator.labeling.Labeler;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class DatasetManager implements LabeledSampleProvider {
    private static final int MAX_DATASET_SIZE = 500;
    private static final int MIN_DATASET_SIZE = 10;

    private final Jdbi jdbi;
    private final int targetDatasetSize;
    private final SetupStrategy setupStrategy;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;
    private final SampleRepository sampleRepository;
    private final PostGenerator postGenerator;
    private final UserGenerator userGenerator;
    private final Labeler labeler;
    private volatile boolean isInitialized = false;
    private final AtomicInteger currentOffset = new AtomicInteger(0);

    public DatasetManager(
            Jdbi jdbi,
            int targetDatasetSize,
            SetupStrategy setupStrategy,
            PostRepository postRepository,
            UserRepository userRepository,
            LabelRepository labelRepository,
            SampleRepository sampleRepository,
            UserGenerator userGenerator,
            PostGenerator postGenerator,
            Labeler labeler
    ) {
        this.jdbi = jdbi;
        this.targetDatasetSize = targetDatasetSize;
        validateDatasetSize();
        this.setupStrategy = setupStrategy;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.labelRepository = labelRepository;
        this.sampleRepository = sampleRepository;
        this.userGenerator = userGenerator;
        this.postGenerator = postGenerator;
        this.labeler = labeler;
    }

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
        isInitialized = true;
    }

    private void handleClearStrategy() {
        deleteAll();
        generateAndSaveSamples(targetDatasetSize);
        doLabeling();
    }

    private void deleteAll() {
        jdbi.useTransaction(handle -> {
            postRepository.deleteAll();
            userRepository.deleteAll();
            labelRepository.deleteAll();
        });
    }

    private void handleReuseStrictStrategy() {
        int currentCount = postRepository.count();
        int countToMake = targetDatasetSize - currentCount;
        if (countToMake < 0) {
            throw new IllegalStateException("기존 데이터가 필요한 데이터보다 많습니다. SetupStrategy을 CLEAR 혹은 REUSE_RANDOM으로 변경하세요.");
        }
        generateAndSaveSamples(countToMake);
        doLabeling();
    }

    private void handleReuseRandomStrategy() {
        int currentCount = postRepository.count();
        int countToMake = targetDatasetSize - currentCount;
        generateAndSaveSamples(countToMake);
        doLabeling();
    }

    private void generateAndSaveSamples(int count) {
        generateAndSaveUsers(count);
        generateAndSavePosts(count);
    }

    private void generateAndSaveUsers(int count) {
        if (count <= 0) return;
        log.info("유저: {}개의 데이터를 {} 모델을 통해 생성합니다.", count, postGenerator.getModelName());
        userRepository.saveAll(userGenerator.generate(count));
    }

    private void generateAndSavePosts(int count) {
        if (count <= 0) return;
        log.info("게시글: {}개의 데이터를 {} 모델을 통해 생성합니다.", count, postGenerator.getModelName());
        postRepository.saveAll(postGenerator.generate(count));
    }

    private void doLabeling() {
        List<UnlabeledSample> unlabeledSamples = sampleRepository.findUnlabeledSamples();
        if(unlabeledUserIds.isEmpty()) return;
        log.info("레이블링되지 않은 {}개의 데이터를 레이블링 합니다.", unlabeledUserIds.size());
        List<User> unlabeledUsers = userRepository.findByIdIn(unlabeledUserIds);
        List<Post> posts = postRepository.findAll();
        labelRepository.saveAll(labeler.label(unlabeledUsers, posts));
    }

    private void validateDatasetSize() {
        if (targetDatasetSize < MIN_DATASET_SIZE || targetDatasetSize > MAX_DATASET_SIZE)
            throw new IllegalArgumentException("데이터 셋 사이즈는 " + MIN_DATASET_SIZE + "에서" + MAX_DATASET_SIZE + "사이 이여야합니다.");
    }

    @Override
    public List<LabeledSample> next(int batchSize) {
        if(!isInitialized) throw new IllegalStateException("데이터셋이 준비되어 있지 않습니다.");
        return List.of();
    }

    @Override
    public int getTotalSize() {
        return 0;
    }

    @Override
    public int getLeftSize() {
        return 0;
    }
}
