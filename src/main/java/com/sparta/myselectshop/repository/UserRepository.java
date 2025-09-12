package com.sparta.myselectshop.repository;

import com.sparta.myselectshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // username 체크
    Optional<User> findByUsername(String username);

    // email
    Optional<User> findByEmail(String email);
}