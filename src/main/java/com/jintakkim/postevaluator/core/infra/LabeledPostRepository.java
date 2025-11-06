package com.jintakkim.postevaluator.core.infra;

import com.jintakkim.postevaluator.core.LabeledPost;

import java.util.List;

public interface LabeledPostRepository {
    void save(LabeledPost post);
    List<Long> findUnlabeledFeatureIds();
    List<LabeledPost> findAll();
}
