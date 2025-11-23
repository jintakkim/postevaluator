package com.jintakkim.postevaluator.persistance;

import com.jintakkim.postevaluator.Label;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.statement.PreparedBatch;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class JdbiLabelRepository implements LabelRepository {
    private static final String INSERT_SQL = """
            INSERT INTO label
                (post_id, user_id, score, reasoning)
            VALUES
                (:postId, :userId, :score, :reasoning)
            """;

    private final JdbiContext jdbiContext;

    @Override
    public Label save(Label post) {
        long id = jdbiContext.withHandle(handle ->
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
        jdbiContext.useHandle(handle -> {
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

//    @Override
//    public List<Long> findUnlabeledUserIds() {
//        String sql = """
//            SELECT u.id
//            FROM User u
//            WHERE NOT EXISTS (
//                SELECT 1
//                FROM label l
//                WHERE l.user_id = p.id
//            )
//            """;
//
//        return jdbi.withHandle(handle ->
//                handle.createQuery(sql)
//                        .mapTo(Long.class)
//                        .list()
//        );
//    }

    @Override
    public List<Label> findAll() {
        String sql = "SELECT * FROM label";
        return jdbiContext.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Label.class)
                        .list()
        );
    }


    @Override
    public void deleteAll() {
        String sql = "DELETE FROM label";
        jdbiContext.useHandle(handle ->
                handle.createUpdate(sql)
                        .execute()
        );
    }

    @Override
    public int count() {
        String sql = "SELECT COUNT(*) FROM label";
        return jdbiContext.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }
}
