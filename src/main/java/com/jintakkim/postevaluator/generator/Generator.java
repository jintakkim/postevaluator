package com.jintakkim.postevaluator.generator;

import com.jintakkim.postevaluator.core.LabeledPost;

import java.util.List;

public interface Generator {
    List<LabeledPost> generate(int size);
}
