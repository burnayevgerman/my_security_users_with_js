package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService,
                           RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String showAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users_list";
    }

    @GetMapping("/create")
    public String createPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "register";
    }

    @PostMapping("/create")
    public String createUser(@RequestParam(value = "selectedRoles", required = false) Set<Long> selectedRoles,
                             @ModelAttribute(name = "user") User user) {
        if (selectedRoles != null) {
            user.setRoles(selectedRoles.stream()
                    .map(roleService::findRoleById)
                    .collect(Collectors.toSet()));
        } else {
            user.setRoles(new HashSet<>());
        }

        if (userService.createUser(user) == null) {
            System.out.println("Failed to create a new user!");
            System.out.println(user);
            return "redirect:/create?error";
        }

        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "edit";
    }

    @PutMapping("/edit")
    public String editUser(@RequestParam(value = "selectedRoles", required = false) Set<Long> selectedRoles,
                           @ModelAttribute("user") User user) {
        try {
            if (selectedRoles != null) {
                user.setRoles(selectedRoles.stream()
                        .map(roleService::findRoleById)
                        .collect(Collectors.toSet()));
            } else {
                user.setRoles(new HashSet<>());
            }

            if (userService.updateUser(user) == null) {
                System.out.println("The changes could not be saved to the database!");
                System.out.println(user);
                return String.format("redirect:/admin/edit/%d?error", user.getId());
            }
            return "redirect:/admin";
        } catch (Exception e) {
            e.printStackTrace();
            return String.format("redirect:/admin/edit/%d?error", user.getId());
        }
    }

    @PutMapping("/edit-password")
    public String editPassword(@RequestParam("id") long id,
                               @RequestParam("password") String password) {
        if (!userService.updatePassword(id, password)) {
            System.out.println("Failed to update the password for the id: " + id);
            return String.format("redirect:/admin/edit/%d/password_error", id);
        }

        return "redirect:/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        if (!userService.deleteUserById(id)) {
            System.out.println("Couldn't delete user with id: " + id);
        }

        return "redirect:/admin";
    }
}
