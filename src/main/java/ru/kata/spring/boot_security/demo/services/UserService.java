package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(long id);
    User getUserByEmail(String email);
    User createUser(User user);
    User updateUser(User user);
    boolean updatePassword(long id, String password);
    boolean deleteUserById(long id);
}
