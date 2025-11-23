package com.jintakkim.postevaluator.core;

/**
 * 알고리즘을 통한 평가에 사용되는 단위
 */
public record LabeledSample(
        User user,
        Post post,
        double labelScore,
        String labelReason
) implements Sample {}
