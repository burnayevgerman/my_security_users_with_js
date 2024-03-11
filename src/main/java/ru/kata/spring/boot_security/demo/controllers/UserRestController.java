package ru.kata.spring.boot_security.demo.controllers;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.io.Serializable;
import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user-info")
    public ResponseEntity<User> getUser(Principal principal) {
        User u = userService.getUserByEmail(principal.getName());
        u.setPassword(null);
        return ResponseEntity.ok(u);
    }
}
