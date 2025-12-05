package com.jintakkim.postevaluator;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 파이프라인 알고리즘 등록
 */
@Getter
public class PipelineAlgorithmRegistry {
    private final List<PipelineRecommendAlgorithm> subAlgorithms = new ArrayList<>();
    private PipelineRecommendAlgorithm finalAlgorithm;

    /**
     * 순서에 민감하다. 예를 들어 1번째로 등록한 알고리즘에서 3번째로 등록한 알고리즘의 결과를 쓰면 예외가 발생한다.
     */
    public void registerSubAlgorithm(PipelineRecommendAlgorithm algorithm) {
        validateDuplicateAlgoName(algorithm.name());
        subAlgorithms.add(algorithm);
    }

    public void registerFinalAlgorithm(PipelineRecommendAlgorithm algorithm) {
        validateDuplicateAlgoName(algorithm.name());
        finalAlgorithm = algorithm;
    }

    private void validateDuplicateAlgoName(String name) {
        boolean isDuplicateInSub = subAlgorithms.stream()
                .anyMatch(existing -> existing.name().equals(name));
        boolean isDuplicateWithFinal = finalAlgorithm != null && finalAlgorithm.name().equals(name);
        if (isDuplicateInSub || isDuplicateWithFinal) {
            throw new IllegalArgumentException("이미 등록된 알고리즘 이름입니다: " + name);
        }
    }

}
