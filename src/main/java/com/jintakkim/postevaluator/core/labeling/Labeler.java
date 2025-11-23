package com.jintakkim.postevaluator.core.labeling;

import com.jintakkim.postevaluator.core.Label;
import com.jintakkim.postevaluator.core.Post;
import com.jintakkim.postevaluator.core.User;

import java.util.List;

public interface Labeler {
    List<Label> label(List<User> users, List<Post> posts);
    String getModelName();
}
