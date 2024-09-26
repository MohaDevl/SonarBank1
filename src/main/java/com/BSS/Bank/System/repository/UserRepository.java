package com.BSS.Bank.System.repository;

import com.BSS.Bank.System.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Here you can define custom queries, for example:
    User findByUsername(String username);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsernameAndPassword(String username, String password);

    User findByEmail(String email);

    // You can add more methods if needed
}