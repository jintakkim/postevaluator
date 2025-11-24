package com.jintakkim.postevaluator.persistance;

import com.jintakkim.postevaluator.*;
import com.jintakkim.postevaluator.generation.PostGenerator;
import com.jintakkim.postevaluator.generation.UserGenerator;
import com.jintakkim.postevaluator.labeling.Labeler;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Slf4j
public class DatasetManager implements LabeledSampleProvider {
    private static final int MAX_DATASET_SIZE = 200;
    private static final int MIN_DATASET_SIZE = 1;

    private final JdbiContext jdbiContext;
    private final int targetUserDatasetSize;
    private final int targetPostDatasetSize;
    private final int sampleDatasetSize;
    private final SetupStrategy setupStrategy;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;
    private final SampleRepository sampleRepository;
    private final PostGenerator postGenerator;
    private final UserGenerator userGenerator;
    private final Labeler labeler;
    private volatile boolean isInitialized = false;

    public DatasetManager(
            JdbiContext jdbiContext,
            int targetUserDatasetSize,
            int targetPostDatasetSize,
            SetupStrategy setupStrategy,
            PostRepository postRepository,
            UserRepository userRepository,
            LabelRepository labelRepository,
            SampleRepository sampleRepository,
            UserGenerator userGenerator,
            PostGenerator postGenerator,
            Labeler labeler
    ) {
        this.jdbiContext = jdbiContext;
        this.targetUserDatasetSize = targetUserDatasetSize;
        this.targetPostDatasetSize = targetPostDatasetSize;
        this.sampleDatasetSize = targetPostDatasetSize * targetUserDatasetSize;
        validateDatasetSize();
        log.info("유저 데이터 셋 사이즈: {}, 게시물 데이터 셋 사이즈: {} => 게시물 평가 데이터 셋 사이즈는 이들간 cross 조합이기 떄문에 {} 개입니다.",
                targetUserDatasetSize, targetPostDatasetSize, sampleDatasetSize);
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
            case REUSE_PARTITION:
                handleReusePartitionStrategy();
                break;
        }
        isInitialized = true;
    }

    private void handleClearStrategy() {
        deleteAll();
        generateAndSaveSamples(targetUserDatasetSize, targetPostDatasetSize);
        doLabeling();
    }

    private void deleteAll() {
        jdbiContext.useHandle(_ -> {
            postRepository.deleteAll();
            userRepository.deleteAll();
            labelRepository.deleteAll();
        });
    }

    private void handleReuseStrictStrategy() {
        int currentUserCount = userRepository.count();
        int currentPostCount = postRepository.count();
        int userCountToMake = targetUserDatasetSize - currentUserCount;
        int postCountToMake = targetPostDatasetSize - currentPostCount;
        if (userCountToMake < 0 || postCountToMake < 0) {
            throw new IllegalStateException("캐시된 데이터가 필요한 데이터보다 많습니다. SetupStrategy을 CLEAR 혹은 REUSE_PARTITION으로 변경하세요.");
        }
        generateAndSaveSamples(userCountToMake, postCountToMake);
        doLabeling();
    }

    private void handleReusePartitionStrategy() {
        int currentUserCount = userRepository.count();
        int currentPostCount = postRepository.count();
        int userCountToMake = targetUserDatasetSize - currentUserCount;
        int postCountToMake = targetPostDatasetSize - currentPostCount;
        generateAndSaveSamples(userCountToMake, postCountToMake);
        doLabeling();
    }

    private void generateAndSaveSamples(int userCount, int postCount) {
        generateAndSaveUsers(userCount);
        generateAndSavePosts(postCount);
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
        if(unlabeledSamples.isEmpty()) return;
        log.info("레이블링되지 않은 {}개의 데이터를 레이블링 합니다.", unlabeledSamples.size());
        labelRepository.saveAll(labeler.label(unlabeledSamples));
    }

    private void validateDatasetSize() {
        if (targetPostDatasetSize < MIN_DATASET_SIZE || targetPostDatasetSize> MAX_DATASET_SIZE || targetUserDatasetSize < MIN_DATASET_SIZE || targetUserDatasetSize > MAX_DATASET_SIZE)
            throw new IllegalArgumentException("유저 혹은 게시물 데이터 셋 사이즈는 " + MIN_DATASET_SIZE + "에서" + MAX_DATASET_SIZE + "사이 이여야합니다.");
    }

    @Override
    public List<LabeledSample> get(int offset, int limit) {
        validateInitialized();
        if (offset >= sampleDatasetSize) return Collections.emptyList();
        int remaining = sampleDatasetSize - offset;
        int effectiveLimit = Math.min(limit, remaining);
        return sampleRepository.findLabeledSamples(offset, effectiveLimit);
    }

    @Override
    public List<LabeledSample> getAll() {
        validateInitialized();
        return sampleRepository.findLabeledSamples(0, sampleDatasetSize);
    }

    private void validateInitialized() {
        if(!isInitialized) throw new IllegalStateException("데이터셋이 준비되어 있지 않습니다.");
    }

    @Override
    public int getTotalSize() {
        //라벨링 데이터 개수는 실질적으로 labeledSample 개수이다.
        int dbCount = labelRepository.count();
        return Math.min(dbCount, sampleDatasetSize);
    }
}
