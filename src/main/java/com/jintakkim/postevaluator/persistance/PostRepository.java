package com.jintakkim.postevaluator.persistance;

import com.jintakkim.postevaluator.Post;

import java.util.Collection;
import java.util.List;

public interface PostRepository {
    Post save(Post post);
    void saveAll(Collection<Post> posts);
    List<Post> findAll();
    int count();
    void deleteAll();
}
