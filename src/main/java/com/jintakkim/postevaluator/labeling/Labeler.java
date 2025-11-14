package com.jintakkim.postevaluator.labeling;

import com.jintakkim.postevaluator.core.LabeledPost;
import com.jintakkim.postevaluator.core.PostFeature;

import java.util.List;

public interface Labeler {
    List<LabeledPost> label(List<PostFeature> unlabeledFeatures);
    String getModelName();
}
