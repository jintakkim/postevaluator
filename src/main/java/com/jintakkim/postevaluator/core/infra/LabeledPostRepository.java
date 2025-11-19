package com.jintakkim.postevaluator.core.infra;

import com.jintakkim.postevaluator.core.LabeledPost;

import java.util.List;

public interface LabeledPostRepository {
    LabeledPost save(LabeledPost post);
    List<Long> findUnlabeledPostIds();
    List<LabeledPost> findAll();
    List<LabeledPost> findRandomly(int count);
    void deleteAll();
    int count();
}
