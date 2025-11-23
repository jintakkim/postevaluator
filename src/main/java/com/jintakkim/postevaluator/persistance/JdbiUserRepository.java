package com.jintakkim.postevaluator.persistance;

import com.jintakkim.postevaluator.EntitySchema;
import com.jintakkim.postevaluator.User;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class JdbiUserRepository implements UserRepository {
    private final Jdbi jdbi;
    private final EntitySchema userSchema;

    @Override
    public List<User> findByIdIn(Collection<Long> ids) {
        if(ids == null || ids.isEmpty()) return List.of();
        String sql = "SELECT * FROM user WHERE id IN (<ids>)";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bindList("ids", ids)
                        .mapTo(User.class)
                        .list()
        );
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM user";
        jdbi.useHandle(handle ->
                handle.createUpdate(sql)
                        .execute()
        );
    }

    @Override
    public User save(User user) {
        Long generatedId = DynamicEntityRepositoryUtils.save(jdbi, User.name, userSchema, user);
        return new User(generatedId, user.features());
    }

    @Override
    public void saveAll(Collection<User> users) {
        DynamicEntityRepositoryUtils.batchSave(jdbi, User.name, userSchema, users);
    }
}
