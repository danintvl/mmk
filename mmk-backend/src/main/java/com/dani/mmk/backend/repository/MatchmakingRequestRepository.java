package com.dani.mmk.backend.repository;

import com.dani.mmk.backend.model.MatchmakingRequest;
import com.dani.mmk.backend.model.Player;
import com.dani.mmk.backend.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchmakingRequestRepository extends JpaRepository<MatchmakingRequest, Long> {

    Optional<MatchmakingRequest> findByPlayer(Player player);

    Optional<MatchmakingRequest> findByMatch(Match match);

}