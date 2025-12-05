package com.jintakkim.postevaluator;

public interface SubAlgorithmResultAccessor {
    AlgorithmStatistics getScoreStatistics(String algorithmName);
    double getScore(String algorithmName);
}
