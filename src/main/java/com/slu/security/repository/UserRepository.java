package com.slu.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.slu.model.security.User;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
