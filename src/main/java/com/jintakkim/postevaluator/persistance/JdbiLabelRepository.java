package com.jintakkim.postevaluator.persistance;

import com.jintakkim.postevaluator.Label;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.PreparedBatch;

import java.util.Collection;
import java.util.List;

public class JdbiLabelRepository implements LabelRepository {
    private static final String INSERT_SQL = """
            INSERT INTO label
                (post_id, user_id, score, reasoning)
            VALUES
                (:postId, :userId, :score, :reasoning)
            """;
    private final Jdbi jdbi;

    public JdbiLabelRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Label save(Label post) {
        long id = jdbi.withHandle(handle ->
                handle.createUpdate(INSERT_SQL)
                        .bind("postId", post.postId())
                        .bind("userId", post.userId())
                        .bind("score", post.score())
                        .bind("reasoning", post.reasoning())
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Long.class)
                        .one()
        );
        return new Label(
                id,
                post.postId(),
                post.userId(),
                post.score(),
                post.reasoning()
        );
    }

    @Override
    public void saveAll(Collection<Label> labels) {
        jdbi.useTransaction(handle -> {
            PreparedBatch batch = handle.prepareBatch(INSERT_SQL);
            for (Label label : labels) {
                batch.bind("postId", label.postId())
                        .bind("userId", label.userId())
                        .bind("score", label.score())
                        .bind("reasoning", label.reasoning())
                        .add();
            }
            batch.execute();
        });
    }

    @Override
    public List<Long> findUnlabeledUserIds() {
        String sql = """
            SELECT u.id
            FROM User u
            WHERE NOT EXISTS (
                SELECT 1
                FROM label l
                WHERE l.user_id = p.id
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
        String sql = "SELECT * FROM label";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Label.class)
                        .list()
        );
    }

    @Override
    public List<Label> findRandomly(int count) {
        String sql = """
                    SELECT id, post_id, user_id, score, reasoning
                    FROM label
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
        String sql = "DELETE FROM label";
        jdbi.useHandle(handle ->
                handle.createUpdate(sql)
                        .execute()
        );
    }

    @Override
    public int count() {
        String sql = "SELECT COUNT(*) FROM label";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }
}
