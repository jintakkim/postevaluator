package com.jintakkim.postevaluator.core.infra;

import com.jintakkim.postevaluator.core.LabeledPost;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class LocalFileLabeledPostRepository implements LabeledPostRepository {
    private final Jdbi jdbi;

    public LocalFileLabeledPostRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public LabeledPost save(LabeledPost post) {
        String sql = """
            INSERT INTO labels
                (feature_id, score, reasoning, labeled_model)
            VALUES
                (:featureId, :score, :reasoning, :labeledModel)
            """;
        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bindBean(post)
                        .executeAndReturnGeneratedKeys("id")
                        .map((rs, ctx) -> new LabeledPost(
                                rs.getLong("id"),
                                post.featureId(),
                                post.score(),
                                post.reasoning(),
                                post.model()
                        ))
                        .one()
        );
    }

    @Override
    public List<Long> findUnlabeledFeatureIds() {
        String sql = """
            SELECT f.id
            FROM features f
            WHERE NOT EXISTS (
                SELECT 1
                FROM labels l
                WHERE l.feature_id = f.id
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
        String sql = "SELECT * FROM labels";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(LabeledPost.class)
                        .list()
        );
    }
}
