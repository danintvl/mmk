package com.dani.mmk.backend.repository;

import com.dani.mmk.backend.model.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dani.mmk.backend.model.Match;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long>{

    List<Match> findByStatus(MatchStatus status);
}
