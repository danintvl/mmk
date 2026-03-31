package com.dani.mmk.backend.repository;

import com.dani.mmk.backend.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}