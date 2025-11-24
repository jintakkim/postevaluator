package com.jintakkim.postevaluator.labeling;

import com.jintakkim.postevaluator.Label;
import com.jintakkim.postevaluator.UnlabeledSample;
import com.jintakkim.postevaluator.BatchCallback;

import java.util.List;

public interface Labeler {
    void label(List<UnlabeledSample> unlabeledSamples, BatchCallback<Label> callback);
    String getModelName();
}
