package ru.kata.spring.boot_security.demo.controllers;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kata.spring.boot_security.demo.models.User;

import java.io.Serializable;

@Data
@NoArgsConstructor
class UserRequest implements Serializable {
    private User user;
    private String targetRole;
}
