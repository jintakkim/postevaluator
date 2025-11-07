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
                (:viewCount, :likeCount, :dislikeCount, :commentCount, :content, :createdAt)
            """;

        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bindBean(postFeature)
                        .executeAndReturnGeneratedKeys("id")
                        .map((rs, ctx) -> new PostFeature(
                                rs.getLong("id"),
                                postFeature.viewCount(),
                                postFeature.likeCount(),
                                postFeature.dislikeCount(),
                                postFeature.commentCount(),
                                postFeature.content(),
                                postFeature.createdAt()
                        ))
                        .one()
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
}
