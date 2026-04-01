package com.dani.mmk.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

    private static final int USERNAME_MAX_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size( max = USERNAME_MAX_LENGTH)
    @Column(nullable = false, length = USERNAME_MAX_LENGTH, unique = true)
    private String username;

    @JsonIgnore
    @NotBlank
    @Column(nullable = false, length = 255)
    private String passwordHash;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private boolean active = true;

    @OneToOne(mappedBy = "user")
    private Player player;

    public User() {
    }

    public User(String email, String username, String passwordHash, UserRole role, boolean active) {
        this.email = email;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.active = active;
    }

    // ---------------GETTERS------------- //

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername(){ return username;}

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public Player getPlayer() {
        return player;
    }

    // ---------------SETTERS------------- //

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) { this.username = username; }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}