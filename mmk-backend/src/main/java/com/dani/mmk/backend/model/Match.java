package com.dani.mmk.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime scheduledAt;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchParticipant> participants = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id")
    private Field field;

    //Constructors
    public Match(){}
    public Match(LocalDateTime scheduledAt, MatchStatus status){
        this.scheduledAt = scheduledAt;
        this.status = status;
    }

    //Getters
    public Long getId() {
        return id;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public Field getField() {
        return field;
    }

    public List<MatchParticipant> getParticipants() {
        return participants;
    }

    //Setters
    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public void addParticipant(MatchParticipant participant){
        this.participants.add(participant);
        participant.setMatch(this);
    }

    public void removeParticipant(MatchParticipant participant){
        this.participants.remove(participant);
        participant.setMatch(null);
    }

    public void setParticipants(List<MatchParticipant> participants) {
        this.participants.forEach(p -> p.setMatch(null));
        this.participants.clear();

        if(participants != null){
            participants.forEach(this::addParticipant);
        }
    }
}