package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);

//    @Query("SELECT r FROM Role r JOIN r.users u WHERE u.id = :id")
//    List<Role> getRolesByUserId(long id);
}
