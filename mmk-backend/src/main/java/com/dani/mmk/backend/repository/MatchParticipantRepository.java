package com.dani.mmk.backend.repository;

import com.dani.mmk.backend.model.Match;
import com.dani.mmk.backend.model.MatchParticipant;
import com.dani.mmk.backend.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchParticipantRepository extends JpaRepository<MatchParticipant, Long> {

    Optional<MatchParticipant> findByMatch(Match match);

    Optional<MatchParticipant> findByPlayer(Player player);

}