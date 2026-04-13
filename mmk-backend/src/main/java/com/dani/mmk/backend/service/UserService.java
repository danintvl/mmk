package com.dani.mmk.backend.service;

import com.dani.mmk.backend.model.User;
import com.dani.mmk.backend.model.UserRole;
import com.dani.mmk.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public List<User> findByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    public User updateUser(Long id, User updatedUser) {
        if (updatedUser == null) {
            throw new IllegalArgumentException("updatedUser must not be null");
        }

        User existingUser = getUserById(id);

        if (updatedUser.getEmail() != null
                && !updatedUser.getEmail().equals(existingUser.getEmail())
                && userRepository.existsByEmail(updatedUser.getEmail())) {
            throw new IllegalArgumentException("Email already used");
        }

        if (updatedUser.getUsername() != null
                && !updatedUser.getUsername().equals(existingUser.getUsername())
                && userRepository.findByUsername(updatedUser.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already used");
        }

        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getUsername() != null) {
            existingUser.setUsername(updatedUser.getUsername());
        }

        if (updatedUser.getPasswordHash() != null && !updatedUser.getPasswordHash().isBlank()) {
            existingUser.setPasswordHash(updatedUser.getPasswordHash());
        }

        if (updatedUser.getRole() != null) {
            existingUser.setRole(updatedUser.getRole());
        }

        return userRepository.save(existingUser);
    }

    public User deactivateUser(Long id) {
        User user = getUserById(id);
        user.setActive(false);
        return userRepository.save(user);
    }
}