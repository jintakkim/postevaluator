package com.jintakkim.postevaluator.generation;

import com.jintakkim.postevaluator.Post;

import java.util.List;

public interface PostGenerator {
    List<Post> generate(int size);
    String getModelName();
}
