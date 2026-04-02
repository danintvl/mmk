package com.dani.mmk.backend.repository;

import com.dani.mmk.backend.model.Match;
import com.dani.mmk.backend.model.MatchmakingRequest;
import com.dani.mmk.backend.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchmakingRequestRepository extends JpaRepository<MatchmakingRequest, Long> {}