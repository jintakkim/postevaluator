package com.jintakkim.postevaluator.labeling;

import com.jintakkim.postevaluator.Label;
import com.jintakkim.postevaluator.UnlabeledSample;

import java.util.List;

public interface Labeler {
    List<Label> label(List<UnlabeledSample> unlabeledSamples);
    String getModelName();
}
