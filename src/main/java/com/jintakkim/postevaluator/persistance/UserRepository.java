package com.jintakkim.postevaluator.persistance;

import com.jintakkim.postevaluator.User;

import java.util.Collection;
import java.util.List;

public interface UserRepository {
    List<User> findByIdIn(Collection<Long> ids);
    List<User> findAll();
    void deleteAll();
    int count();
    User save(User user);
    void saveAll(Collection<User> users);
}
