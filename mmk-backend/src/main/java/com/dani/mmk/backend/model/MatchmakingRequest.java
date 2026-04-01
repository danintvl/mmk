package com.dani.mmk.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;import java.util.List;

@Entity
@Table(name = "matchmaking_requests")
public class MatchmakingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @NotEmpty
    @OneToMany(mappedBy = "matchmakingRequest", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AvailabilitySlot> availabilitySlots = new ArrayList<>();

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchmakingRequestStatus status;

    @ManyToMany
    @JoinTable(
            name = "matchmaking_request_acceptable_fields",
            joinColumns = @JoinColumn(name = "matchmaking_request_id"),
            inverseJoinColumns = @JoinColumn(name = "field_id")
    )
    @NotEmpty
    private List<Field> acceptableFields;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matched_match_id")
    private Match matchedMatch;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected MatchmakingRequest(){}

    //--------------------------GETTERS----------------------------//
    public List<AvailabilitySlot> getAvailabilitySlots() {
        return availabilitySlots;
    }

    public Long getId() {
        return id;
    }
    public List<Field> getAcceptableFields() {
        return acceptableFields;
    }
    public Match getMatchedMatch() {
        return matchedMatch;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public MatchmakingRequestStatus getStatus() {
        return status;
    }
    public Player getPlayer() {
        return player;
    }

    //--------------------------SETTERS---------------------------//

    public void addAvailabilitySlot(AvailabilitySlot slot){
        this.availabilitySlots.add(slot);
        slot.setMatchmakingRequest(this);
    }

    public void removeAvailabilitySlot(AvailabilitySlot slot){
        this.availabilitySlots.remove(slot);
        slot.setMatchmakingRequest(null);
    }

    public void setAvailabilitySlots(List<AvailabilitySlot> availabilitySlots) {
        this.availabilitySlots.forEach(slot -> slot.setMatchmakingRequest(null));
        this.availabilitySlots.clear();

        if (availabilitySlots != null){
            availabilitySlots.forEach(this::addAvailabilitySlot);
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setAcceptableFields(List<Field> acceptableFields) {
        this.acceptableFields = acceptableFields;
    }

    public void setStatus(MatchmakingRequestStatus status) {
        this.status = status;
    }

    public void setMatchedMatch(Match matchedMatch) {
        this.matchedMatch = matchedMatch;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
