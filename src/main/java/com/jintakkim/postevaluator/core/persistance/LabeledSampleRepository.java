package com.jintakkim.postevaluator.core.persistance;

import com.jintakkim.postevaluator.core.LabeledSample;

import java.util.List;

public interface LabeledSampleRepository {
    List<LabeledSample> findAll(int offset, int limit);
}
