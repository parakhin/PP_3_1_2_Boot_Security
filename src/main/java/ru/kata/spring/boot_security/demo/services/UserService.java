package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();

    User findOne(Long id);

    void save(User user);

    void update(Long id, User user);


    void delete(Long id);

    Optional<User> loadUserByEmail(String username);
}
