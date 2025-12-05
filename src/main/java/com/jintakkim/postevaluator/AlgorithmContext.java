package com.jintakkim.postevaluator;

import com.jintakkim.postevaluator.evaluation.SamplePrediction;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class AlgorithmContext implements SubAlgorithmResultAccessor {
    private final Map<String, AlgorithmStatistics> statistics = new HashMap<>();
    private final Map<String, Map<SampleKey, Double>> scores = new HashMap<>();

    private SampleKey currentSample;

    public void setCurrentSample(long userId, long postId) {
        this.currentSample = new SampleKey(userId, postId);
    }

    @Override
    public AlgorithmStatistics getScoreStatistics(String algorithmName) {
        AlgorithmStatistics found = statistics.get(algorithmName);
        if (found == null)
            throw new IllegalArgumentException("해당 알고리즘 결과값을 찾을 수 없습니다.");
        return found;
    }

    @Override
    public double getScore(String algorithmName) {
        Map<SampleKey, Double> algoScores = scores.get(algorithmName);
        if (algoScores == null)
            throw new IllegalArgumentException("해당 알고리즘 결과값을 찾을 수 없습니다: " + algorithmName);
        Double score = algoScores.get(currentSample);
        if (score == null)
            throw new IllegalArgumentException("현재 샘플에 대한 스코어를 찾을 수 없습니다.");
        return score;
    }

    public void add(String algoName, List<SamplePrediction> predictions, AlgorithmStatistics stats) {
        statistics.put(algoName, stats);
        Map<SampleKey, Double> algoScores = new HashMap<>();
        for (SamplePrediction p : predictions)
            algoScores.put(new SampleKey(p.userId(), p.postId()), p.predictScore());
        scores.put(algoName, algoScores);
    }

    private record SampleKey(long userId, long postId) {}
}