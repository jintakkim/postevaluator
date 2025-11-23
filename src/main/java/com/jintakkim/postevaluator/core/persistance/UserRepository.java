package com.jintakkim.postevaluator.core.persistance;

import com.jintakkim.postevaluator.core.User;

import java.util.Collection;
import java.util.List;

public interface UserRepository {
    List<User> findByIdIn(Collection<Long> ids);
    void deleteAll();
    User save(User user);
    void saveAll(Collection<User> users);
}
