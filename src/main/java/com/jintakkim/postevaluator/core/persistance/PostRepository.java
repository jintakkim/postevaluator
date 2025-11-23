package com.jintakkim.postevaluator.core.persistance;

import com.jintakkim.postevaluator.core.Post;

import java.util.Collection;
import java.util.List;

public interface PostRepository {
    Post save(Post post);
    void saveAll(Collection<Post> posts);
    List<Post> findAll();
    int count();
    void deleteAll();
}
