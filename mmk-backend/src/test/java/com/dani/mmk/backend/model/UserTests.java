package com.dani.mmk.backend.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTests {

    private ValidatorFactory factory;
    private Validator validator;

    @BeforeEach
    void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterEach
    void tearDown() {
        if (factory != null) {
            factory.close();
        }
    }

    @Test
    void shouldCreateUserWithConstructor() {
        User user = new User(
                "test@example.com",
                "testuser",
                "hashedPassword",
                UserRole.PLAYER,
                true
        );

        assertEquals("test@example.com", user.getEmail());
        assertEquals("testuser", user.getUsername());
        assertEquals("hashedPassword", user.getPasswordHash());
        assertEquals(UserRole.PLAYER, user.getRole());
        assertTrue(user.isActive());
    }

    @Test
    void shouldHaveActiveTrueByDefault() {
        User user = new User();

        assertTrue(user.isActive());
    }

    @Test
    void shouldSetAndGetFields() {
        User user = new User();

        user.setEmail("john@example.com");
        user.setUsername("john");
        user.setPasswordHash("hash");
        user.setRole(UserRole.ADMIN);
        user.setActive(false);

        assertEquals("john@example.com", user.getEmail());
        assertEquals("john", user.getUsername());
        assertEquals("hash", user.getPasswordHash());
        assertEquals(UserRole.ADMIN, user.getRole());
        assertFalse(user.isActive());
    }

    @Test
    void shouldBeValidWhenUserIsCorrect() {
        User user = new User(
                "test@example.com",
                "testuser",
                "hashedPassword",
                UserRole.PLAYER,
                true
        );

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeInvalidWhenEmailIsBlank() {
        User user = new User(
                "",
                "testuser",
                "hashedPassword",
                UserRole.PLAYER,
                true
        );

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertTrue(
                violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email"))
        );
    }

    @Test
    void shouldBeInvalidWhenUsernameIsBlank() {
        User user = new User(
                "test@example.com",
                "",
                "hashedPassword",
                UserRole.PLAYER,
                true
        );

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertTrue(
                violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("username"))
        );
    }

    @Test
    void shouldBeInvalidWhenPasswordHashIsBlank() {
        User user = new User(
                "test@example.com",
                "testuser",
                "",
                UserRole.PLAYER,
                true
        );

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertTrue(
                violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("passwordHash"))
        );
    }

    @Test
    void shouldBeInvalidWhenRoleIsNull() {
        User user = new User(
                "test@example.com",
                "testuser",
                "hashedPassword",
                null,
                true
        );

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertTrue(
                violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("role"))
        );
    }

    @Test
    void shouldLinkPlayerToUser() {
        User user = new User(
                "test@example.com",
                "testuser",
                "hashedPassword",
                UserRole.PLAYER,
                true
        );

        Player player = new Player(user);
        user.setPlayer(player);

        assertSame(player, user.getPlayer());
        assertSame(user, player.getUser());
    }
}