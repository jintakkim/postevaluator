package com.jintakkim.postevaluator.labeling;

import com.jintakkim.postevaluator.core.Label;
import com.jintakkim.postevaluator.core.Post;

import java.util.List;

public interface Labeler {
    List<Label> label(List<Post> unlabeledPosts);
    String getModelName();
}
