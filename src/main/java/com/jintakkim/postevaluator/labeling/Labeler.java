package com.jintakkim.postevaluator.labeling;

import com.jintakkim.postevaluator.Label;
import com.jintakkim.postevaluator.Post;
import com.jintakkim.postevaluator.User;

import java.util.List;

public interface Labeler {
    List<Label> label(List<User> users, List<Post> posts);
    String getModelName();
}
