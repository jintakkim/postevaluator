package com.jintakkim.postevaluator.core.infra;

import com.jintakkim.postevaluator.core.PostFeature;

import java.util.Collection;
import java.util.List;

public interface PostFeatureRepository {
    void save(PostFeature postFeature);
    List<PostFeature> findByIdIn(Collection<Long> ids);
}
