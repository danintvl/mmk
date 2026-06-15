package com.dani.mmk.backend.repository;

import com.dani.mmk.backend.model.MatchmakingRequest;
import com.dani.mmk.backend.model.Match;
import com.dani.mmk.backend.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchmakingRequestRepository extends JpaRepository<MatchmakingRequest, Long> {

    // A player can have multiple matchmaking requests
    List<MatchmakingRequest> findByPlayer(Player player);

    // Convenience method: find requests by player id without loading the Player entity
    List<MatchmakingRequest> findByPlayerId(Long playerId);

    Optional<MatchmakingRequest> findByMatchedMatch(Match matchedMatch);

}