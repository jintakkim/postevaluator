package com.jintakkim.postevaluator.persistance;

import com.jintakkim.postevaluator.EntitySchema;
import com.jintakkim.postevaluator.User;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class JdbiUserRepository implements UserRepository {
    private final JdbiContext jdbiContext;
    private final EntitySchema userSchema;

    @Override
    public List<User> findByIdIn(Collection<Long> ids) {
        if(ids == null || ids.isEmpty()) return List.of();
        String sql = "SELECT * FROM user WHERE id IN (<ids>)";
        return jdbiContext.withHandle(handle ->
                handle.createQuery(sql)
                        .bindList("ids", ids)
                        .mapTo(User.class)
                        .list()
        );
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM user";
        return jdbiContext.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(User.class)
                        .list()
        );
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM user";
        jdbiContext.useHandle(handle ->
                handle.createUpdate(sql)
                        .execute()
        );
    }

    @Override
    public User save(User user) {
        Long generatedId = DynamicEntityRepositoryUtils.save(jdbiContext, User.name, userSchema, user);
        return new User(generatedId, user.features());
    }

    @Override
    public void saveAll(Collection<User> users) {
        DynamicEntityRepositoryUtils.batchSave(jdbiContext, User.name, userSchema, users);
    }
}
