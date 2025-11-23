package com.jintakkim.postevaluator.persistance;

import com.jintakkim.postevaluator.LabeledSample;
import com.jintakkim.postevaluator.UnlabeledSample;

import java.util.List;

public interface SampleRepository {
    List<LabeledSample> findLabeledSamples(int offset, int limit);
    List<LabeledSample> findLabeledSamples();
    List<UnlabeledSample> findUnlabeledSamples(int offset, int limit);
    List<UnlabeledSample> findUnlabeledSamples();
}
