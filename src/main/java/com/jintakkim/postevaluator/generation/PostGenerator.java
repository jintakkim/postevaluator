package com.jintakkim.postevaluator.generation;

import com.jintakkim.postevaluator.Post;
import com.jintakkim.postevaluator.gemini.BatchCallback;

public interface PostGenerator {
    void generate(int size, BatchCallback<Post> callback);
    String getModelName();
}
