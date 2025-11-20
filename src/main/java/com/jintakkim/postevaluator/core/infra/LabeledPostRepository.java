package com.jintakkim.postevaluator.core.infra;

import com.jintakkim.postevaluator.core.Label;

import java.util.List;

public interface LabeledPostRepository {
    Label save(Label post);
    List<Long> findUnlabeledPostIds();
    List<Label> findAll();
    List<Label> findRandomly(int count);
    void deleteAll();
    int count();
}
