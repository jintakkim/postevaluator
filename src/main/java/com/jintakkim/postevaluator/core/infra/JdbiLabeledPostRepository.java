package com.jintakkim.postevaluator.core.infra;

import com.jintakkim.postevaluator.core.LabeledPost;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class JdbiLabeledPostRepository implements LabeledPostRepository {
    private final Jdbi jdbi;

    public JdbiLabeledPostRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public LabeledPost save(LabeledPost post) {
        String sql = """
            INSERT INTO labeled_post
                (feature_id, score, reasoning, model)
            VALUES
                (:featureId, :score, :reasoning, :model)
            """;
        long id = jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("featureId", post.featureId())
                        .bind("score", post.score())
                        .bind("reasoning", post.reasoning())
                        .bind("model", post.model())
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Long.class)
                        .one()
        );
        return new LabeledPost(
                id,
                post.featureId(),
                post.score(),
                post.reasoning(),
                post.model()
        );
    }

    @Override
    public List<Long> findUnlabeledFeatureIds() {
        String sql = """
            SELECT p.id
            FROM post_feature p
            WHERE NOT EXISTS (
                SELECT 1
                FROM labeled_post l
                WHERE l.feature_id = p.id
            )
            """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Long.class)
                        .list()
        );
    }

    @Override
    public List<LabeledPost> findAll() {
        String sql = "SELECT * FROM labeled_post";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(LabeledPost.class)
                        .list()
        );
    }
}
