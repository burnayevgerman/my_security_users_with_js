package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.Role;

import java.util.Set;

public interface RoleService {
    Role addRole(Role role);
    Role findRoleByName(String name);
    Set<Role> getAllRoles();
    Role findRoleById(long id);

}
