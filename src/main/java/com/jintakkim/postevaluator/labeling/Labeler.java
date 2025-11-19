package com.jintakkim.postevaluator.labeling;

import com.jintakkim.postevaluator.core.LabeledPost;
import com.jintakkim.postevaluator.core.Post;

import java.util.List;

public interface Labeler {
    List<LabeledPost> label(List<Post> unlabeledPosts);
    String getModelName();
}
