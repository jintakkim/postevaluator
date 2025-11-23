package com.jintakkim.postevaluator.persistance;

import com.jintakkim.postevaluator.Label;

import java.util.Collection;
import java.util.List;

public interface LabelRepository {
    Label save(Label label);
    void saveAll(Collection<Label> labels);
    List<Label> findAll();
    void deleteAll();
    int count();
}
