package com.jintakkim.postevaluator.core.infra;

import com.jintakkim.postevaluator.core.LabeledPost;

import java.util.List;

public interface LabeledPostRepository {
    LabeledPost save(LabeledPost post);
    List<Long> findUnlabeledFeatureIds();
    List<LabeledPost> findAll();
    void deleteAll();
}
