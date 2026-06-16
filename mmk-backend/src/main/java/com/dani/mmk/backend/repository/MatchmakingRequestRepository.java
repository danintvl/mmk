package com.dani.mmk.backend.repository;

import com.dani.mmk.backend.model.MatchmakingRequest;
import com.dani.mmk.backend.model.Match;
import com.dani.mmk.backend.model.MatchmakingRequestStatus;
import com.dani.mmk.backend.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchmakingRequestRepository extends JpaRepository<MatchmakingRequest, Long> {

    List<MatchmakingRequest> findByPlayer(Player player);

    List<MatchmakingRequest> findByPlayerId(Long playerId);

    Optional<MatchmakingRequest> findByMatchedMatch(Match matchedMatch);

    List<MatchmakingRequest> findByStatus(MatchmakingRequestStatus status);

}