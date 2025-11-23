package com.jintakkim.postevaluator.persistance;

import com.jintakkim.postevaluator.EntitySchema;
import com.jintakkim.postevaluator.Post;

import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class JdbiPostRepository implements PostRepository {
    private final Jdbi jdbi;
    private final EntitySchema postSchema;

    @Override
    public Post save(Post post) {
        Long generatedId = DynamicEntityRepositoryUtils.save(jdbi, Post.name, postSchema, post);
        return new Post(generatedId, post.features());
    }

    @Override
    public void saveAll(Collection<Post> posts) {
        DynamicEntityRepositoryUtils.batchSave(jdbi, Post.name, postSchema, posts);
    }

    @Override
    public List<Post> findAll() {
        String sql = "SELECT * FROM post";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Post.class)
                        .list()
        );
    }

//    @Override
//    public List<Post> findByIdIn(Collection<Long> ids) {
//        if(ids == null || ids.isEmpty()) return List.of();
//        String sql = "SELECT * FROM post WHERE id IN (<ids>)";
//        return jdbi.withHandle(handle ->
//                handle.createQuery(sql)
//                        .bindList("ids", ids)
//                        .mapTo(Post.class)
//                        .list()
//        );
//    }

    @Override
    public int count() {
        String sql = "SELECT COUNT(*) FROM post";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM post";
        jdbi.useHandle(handle ->
                handle.createUpdate(sql)
                        .execute()
        );
    }
}
