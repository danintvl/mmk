package com.dani.mmk.backend.service;

import com.dani.mmk.backend.model.User;
import com.dani.mmk.backend.model.UserRole;
import com.dani.mmk.backend.model.Player;
import com.dani.mmk.backend.repository.PlayerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final UserService userService;

    public PlayerService(PlayerRepository playerRepository, UserService userService) {
        this.playerRepository = playerRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public Player getPlayerById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Player with id " + id + " not found"));
    }

    @Transactional(readOnly = true)
    public Player getPlayerByUserId(Long userId) {
        return playerRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Player with user id " + userId + " not found"));
    }

    public Player createPlayer(Long userId) {
        User user = userService.getUserById(userId);

        if (!user.isActive()) {
            throw new IllegalStateException("Cannot create player for inactive user id " + userId);
        }

        if (user.getRole() != UserRole.PLAYER) {
            throw new IllegalArgumentException("Cannot create player for non-PLAYER user id " + userId);
        }

        if (playerRepository.findByUserId(userId).isPresent()) {
            throw new IllegalArgumentException("Player for user id " + userId + " already exists");
        }

        Player player = new Player(user);
        return playerRepository.save(player);
    }
}