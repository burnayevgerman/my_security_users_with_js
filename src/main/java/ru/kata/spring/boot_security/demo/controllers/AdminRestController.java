package ru.kata.spring.boot_security.demo.controllers;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.spring5.SpringTemplateEngine;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

import java.util.*;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;

@RestController
@RequestMapping("/admin")
public class AdminRestController {
    private final UserService userService;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;

    public AdminRestController(UserService userService, RoleService roleService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.roleService = roleService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/users-list")
    public ResponseEntity<List<User>> getUsersList() {
        return ResponseEntity.ok(userService.getAllUsers()
                .stream().peek(u -> u.setPassword(null)).toList());
    }

    @GetMapping("/user-info/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        User user = userService.getUserById(id);

        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user = userService.createUser(user);

        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/user-info/{id}")
                        .buildAndExpand(user.getId())
                        .toUri()
        ).body(user);
    }

    private void logoutUser(HttpServletRequest request) {
        SecurityContextHolder.getContext().setAuthentication(null);
        request.getSession().invalidate();
    }


    @PutMapping(path = "/edit")
    public ResponseEntity<User> editUser(@RequestBody UserRequest userRequest,
                                         Principal principal, HttpServletRequest request) {
        User user = userRequest.getUser();
        user.setRoles(new HashSet<>(Set.of(
                roleService.findRoleByName(userRequest.getTargetRole()))));

        boolean isCurrentUser = false;

        if (userService.getUserByEmail(principal.getName()).getId().equals(user.getId())) {
            isCurrentUser = true;
        }

        user = userService.updateUser(user);

        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        if (isCurrentUser) {
            SecurityContext securityContext = SecurityContextHolder.getContext();

            if (!Set.of(securityContext.getAuthentication().getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet()))
                    .equals(Set.of(user.getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toSet())))
                    || !principal.getName().equals(user.getEmail())) {
                logoutUser(request);
                return ResponseEntity.noContent().build();
            }
        }

        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Long> deleteUser(@RequestParam("id") long id,
                                           Principal principal, HttpServletRequest request) {
        User user = userService.getUserById(id);

        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        boolean currentUser = principal.getName().equals(user.getEmail());

        if (!userService.deleteUserById(id)) {
            return ResponseEntity.badRequest().build();
        }

        if (currentUser) {
            logoutUser(request);
        }

        return ResponseEntity.ok(id);
    }
}
