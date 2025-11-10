package com.jintakkim.postevaluator.core.infra;

import com.jintakkim.postevaluator.core.PostFeature;
import org.jdbi.v3.core.Jdbi;

import java.util.Collection;
import java.util.List;

public class JdbiPostFeatureRepository implements PostFeatureRepository {
    private final Jdbi jdbi;

    public JdbiPostFeatureRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public PostFeature save(PostFeature postFeature) {
        String sql = """
            INSERT INTO post_feature
                (view_count, like_count, dislike_count, comment_count, content, created_at)
            VALUES
                (:viewCountConfig, :likeCount, :dislikeCount, :commentCount, :content, :createdAt)
            """;

        long id = jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("viewCountConfig", postFeature.viewCount())
                        .bind("likeCount", postFeature.likeCount())
                        .bind("dislikeCount", postFeature.dislikeCount())
                        .bind("commentCount", postFeature.commentCount())
                        .bind("content", postFeature.content())
                        .bind("createdAt", postFeature.createdAt())
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Long.class)
                        .one());
        return new PostFeature(
                id,
                postFeature.viewCount(),
                postFeature.likeCount(),
                postFeature.dislikeCount(),
                postFeature.commentCount(),
                postFeature.content(),
                postFeature.createdAt()
        );
    }

    @Override
    public List<PostFeature> findByIdIn(Collection<Long> ids) {
        if(ids == null || ids.isEmpty()) return List.of();
        String sql = "SELECT * FROM post_feature WHERE id IN (<ids>)";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bindList("ids", ids)
                        .mapTo(PostFeature.class)
                        .list()
        );
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM post_feature";
        jdbi.useHandle(handle ->
                handle.createUpdate(sql)
                        .execute()
        );
    }
}
