package com.dani.mmk.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "match_participants")
public class MatchParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime joinedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    protected MatchParticipant(){}

    public MatchParticipant(LocalDateTime joinedAt,Player player, Match match){
        this.joinedAt = joinedAt;
        this.player = player;
        this.match = match;
    }

    //---------------GETTERS-----------------//
    public Long getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public Match getMatch() {
        return match;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

//--------------------------------//
//---------------SETTERS-----------------//


    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }
//--------------------------------//
}
