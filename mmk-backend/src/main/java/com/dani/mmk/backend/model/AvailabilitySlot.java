package com.dani.mmk.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "availability_slots")
public class AvailabilitySlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime startAt;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime endAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "matchmaking_request_id", nullable = false)
    private MatchmakingRequest matchmakingRequest;

    protected AvailabilitySlot() {
    }

    public AvailabilitySlot(LocalDateTime startAt, LocalDateTime endAt, MatchmakingRequest matchmakingRequest) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.matchmakingRequest = matchmakingRequest;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public MatchmakingRequest getMatchmakingRequest() {
        return matchmakingRequest;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }

    public void setMatchmakingRequest(MatchmakingRequest matchmakingRequest) {
        this.matchmakingRequest = matchmakingRequest;
    }

    @AssertTrue(message = "startAt must be before endAt")
    public boolean isValidRange() {
        if (startAt == null || endAt == null) {
            return true;
        }
        return startAt.isBefore(endAt);
    }
}