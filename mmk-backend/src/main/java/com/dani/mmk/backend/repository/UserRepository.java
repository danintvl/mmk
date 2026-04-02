package com.dani.mmk.backend.repository;

import com.dani.mmk.backend.model.User;
import com.dani.mmk.backend.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRole(UserRole role);

    Optional<User> findByUsername(String username);
}