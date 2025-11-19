package com.jintakkim.postevaluator.core.infra;

import com.jintakkim.postevaluator.core.Post;

import java.util.Collection;
import java.util.List;

public interface PostRepository {
    Post save(Post post);
    List<Post> findByIdIn(Collection<Long> ids);
    int count();
    void deleteAll();
}
