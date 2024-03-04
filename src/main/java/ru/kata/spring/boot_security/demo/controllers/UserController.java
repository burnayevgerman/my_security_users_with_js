package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.models.*;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    public String userInfo(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);
        model.addAttribute("roles", user.getRoles().stream()
                .map(Role::getName).collect(Collectors.toSet()));
        model.addAttribute("viewRoles", user.getRoles().stream()
                .map(Role::getViewText).collect(Collectors.toSet()));
        return "desktop";
    }
}
