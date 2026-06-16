package com.dani.mmk.backend.service;

import com.dani.mmk.backend.model.User;
import com.dani.mmk.backend.model.UserRole;
import com.dani.mmk.backend.model.Player;
import com.dani.mmk.backend.model.MatchmakingRequest;
import com.dani.mmk.backend.repository.PlayerRepository;
import com.dani.mmk.backend.repository.MatchmakingRequestRepository;
import com.dani.mmk.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;
    private final MatchmakingRequestRepository matchmakingRequestRepository;

    public PlayerService(PlayerRepository playerRepository, UserRepository userRepository,
                        MatchmakingRequestRepository matchmakingRequestRepository) {
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
        this.matchmakingRequestRepository = matchmakingRequestRepository;
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
        User user = userRepository.getReferenceById(userId);

        if (!user.isActive()) {
            throw new IllegalStateException("Cannot create player for inactive user id " + userId);
        }

        if (user.getRole() != UserRole.PLAYER) {
            throw new IllegalArgumentException("Cannot create player for non-PLAYER user id " + userId);
        }

        if (playerRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException("Player for user id " + userId + " already exists");
        }

        Player player = new Player(user);
        return playerRepository.save(player);
    }

    @Transactional(readOnly = true)
    public List<MatchmakingRequest> getPlayerMatchmakingRequests(Long playerId) {

        // L'exception remonte dans le cas où le joueur n'existe pas et la fonction ne retourne pas de List

        Player player = getPlayerById(playerId);
        return matchmakingRequestRepository.findByPlayer(player);
    }
}
