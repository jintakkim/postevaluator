package com.jintakkim.postevaluator.core.infra;

import com.jintakkim.postevaluator.core.Label;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class JdbiLabeledPostRepository implements LabeledPostRepository {
    private final Jdbi jdbi;

    public JdbiLabeledPostRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Label save(Label post) {
        String sql = """
            INSERT INTO labeled_post
                (post_id, score, reasoning)
            VALUES
                (:postId, :score, :reasoning)
            """;
        long id = jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("postId", post.postId())
                        .bind("score", post.score())
                        .bind("reasoning", post.reasoning())
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Long.class)
                        .one()
        );
        return new Label(
                id,
                post.postId(),
                post.score(),
                post.reasoning()
        );
    }

    @Override
    public List<Long> findUnlabeledPostIds() {
        String sql = """
            SELECT p.id
            FROM post p
            WHERE NOT EXISTS (
                SELECT 1
                FROM labeled_post l
                WHERE l.post_id = p.id
            )
            """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Long.class)
                        .list()
        );
    }

    @Override
    public List<Label> findAll() {
        String sql = "SELECT * FROM labeled_post";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Label.class)
                        .list()
        );
    }

    @Override
    public List<Label> findRandomly(int count) {
        String sql = """
                    SELECT id, post_id, score, created_at
                    FROM labeled_post
                    ORDER BY RANDOM()
                    LIMIT :count
                    """;
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("count", count)
                        .mapTo(Label.class)
                        .list()
        );
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM labeled_post";
        jdbi.useHandle(handle ->
                handle.createUpdate(sql)
                        .execute()
        );
    }

    @Override
    public int count() {
        String sql = "SELECT COUNT(*) FROM labeled_post";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }
}
