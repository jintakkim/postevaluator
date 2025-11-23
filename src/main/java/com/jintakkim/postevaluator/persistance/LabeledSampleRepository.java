package com.jintakkim.postevaluator.persistance;

import com.jintakkim.postevaluator.LabeledSample;

import java.util.List;

public interface LabeledSampleRepository {
    List<LabeledSample> findAll(int offset, int limit);
}
