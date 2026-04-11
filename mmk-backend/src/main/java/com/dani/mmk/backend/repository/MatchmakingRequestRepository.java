package com.dani.mmk.backend.repository;

import com.dani.mmk.backend.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchmakingRequestRepository extends JpaRepository<MatchmakingRequest, Long> {

    Optional<MatchmakingRequest> findByPlayer(Player player);

    Optional<MatchmakingRequest> findByMatch(Match match);

}