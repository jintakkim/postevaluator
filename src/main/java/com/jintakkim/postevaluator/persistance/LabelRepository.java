package com.jintakkim.postevaluator.persistance;

import com.jintakkim.postevaluator.Label;

import java.util.Collection;
import java.util.List;

public interface LabelRepository {
    Label save(Label label);
    void saveAll(Collection<Label> labels);
    /**
     * 라벨링되지 않은 User Id 반환
     */
    List<Long> findUnlabeledUserIds();
    List<Label> findAll();
    List<Label> findRandomly(int count);
    void deleteAll();
    int count();
}
