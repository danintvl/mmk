package com.dani.mmk.backend.service;

import com.dani.mmk.backend.model.MatchmakingRequest;
import com.dani.mmk.backend.model.Player;
import com.dani.mmk.backend.repository.FieldRepository;
import com.dani.mmk.backend.repository.MatchmakingRequestRepository;
import com.dani.mmk.backend.repository.PlayerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MatchmakingRequestService {

    private final MatchmakingRequestRepository matchmakingRequestRepository;
    private final PlayerRepository playerRepository;
    private final FieldRepository fieldRepository;

    public MatchmakingRequestService(MatchmakingRequestRepository matchmakingRequestRepository,
                                     PlayerRepository playerRepository,
                                     FieldRepository fieldRepository) {
        this.matchmakingRequestRepository = matchmakingRequestRepository;
        this.playerRepository = playerRepository;
        this.fieldRepository = fieldRepository;
    }

    //Getters

    public MatchmakingRequest getMatchmakingRequestById(Long id) {
        return matchmakingRequestRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("MatchmakingRequest with id " + id + " not found"));
    }

    public List<MatchmakingRequest> getMatchmakingRequestsByPlayerId(Long playerId) {
        return matchmakingRequestRepository.findByPlayerId(playerId);
    }

    public List<MatchmakingRequest> getMatchmakingRequestsByPlayer (Player player) {
        return matchmakingRequestRepository.findByPlayer(player);
    }

}