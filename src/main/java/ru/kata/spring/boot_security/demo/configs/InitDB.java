package ru.kata.spring.boot_security.demo.configs;

import lombok.Lombok;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;
import ru.kata.spring.boot_security.demo.models.*;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

@Component
public class InitDB {
    private final RoleService roleService;
    private final UserService userService;

    public InitDB(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @PostConstruct
    public void initDB() {
        Set<Role> roles = roleService.getAllRoles();

        if (roles.stream().noneMatch(r -> r.getName().contains("USER"))) {
            roleService.addRole(new Role("ROLE_USER", "User", true));
        }

        if (roles.stream().noneMatch(r -> r.getName().contains("ADMIN"))) {
            roleService.addRole(new Role("ROLE_ADMIN", "Admin", false));
        }

        List<User> users = userService.getAllUsers();
        users.forEach(user -> {
            if (user.getRoles().stream().noneMatch(r -> r.getName().contains("USER"))) {
                user.getRoles().add(roleService.findRoleByName("ROLE_USER"));
            }
        });

        if (users.stream().noneMatch(
                u -> u.getRoles().stream().anyMatch(
                        r -> r.getName().contains("ADMIN")
                ))) {
            User mainAdmin = new User(
                    "burnayev_german@mail.ru",
                    "root",
                    "German"
            );
            mainAdmin.getRoles().addAll(List.of(
                    roleService.findRoleByName("ROLE_USER"),
                    roleService.findRoleByName("ROLE_ADMIN")
            ));

            User findUser = users.stream()
                    .filter(u -> u.getEmail().equalsIgnoreCase(mainAdmin.getEmail()))
                    .findAny().orElse(null);

            if (findUser == null) {
                userService.createUser(mainAdmin);
            } else {
                findUser.getRoles().add(roleService.findRoleByName("ROLE_ADMIN"));
                userService.updateUser(findUser);
            }
        }
    }
}
