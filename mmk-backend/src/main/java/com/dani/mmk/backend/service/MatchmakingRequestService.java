package com.dani.mmk.backend.service;

import com.dani.mmk.backend.model.AvailabilitySlot;
import com.dani.mmk.backend.model.Field;
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

    @Transactional(readOnly = true)
    public MatchmakingRequest getMatchmakingRequestById(Long id) {
        return matchmakingRequestRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("MatchmakingRequest with id " + id + " not found"));
    }

    // Cette fonction semble inutile si l'on passe à chaque fois par le joueur, autant directement passer par le service de celui-ci

    /*

    @Transactional(readOnly = true)
    public List<MatchmakingRequest> getMatchmakingRequestsByPlayerId(Long playerId) {
        return matchmakingRequestRepository.findByPlayerId(playerId);
    }

    */

    //Creators

    public MatchmakingRequest createMatchmakingRequest(Long playerId, List<AvailabilitySlot> availabilitySlots, List<Field> acceptableFields){
        //Validation
        if(availabilitySlots == null || availabilitySlots.isEmpty()){
            throw new IllegalArgumentException("availabilitySlots can't be null nor empty");
        }
        if(acceptableFields == null || acceptableFields.isEmpty()){
            throw new IllegalArgumentException("acceptableFields can't be null nor empty");
        }

        Player player = playerRepository.findById(playerId).orElseThrow(() -> new EntityNotFoundException("Player with id : "+ playerId + " not found."));

        //Make a request to save with matchmakingRequestRepo
        MatchmakingRequest request = new MatchmakingRequest(player, availabilitySlots, acceptableFields);

        return matchmakingRequestRepository.save(request);
    }

}