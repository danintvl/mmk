package com.dani.mmk.backend.service;

import com.dani.mmk.backend.model.MatchmakingRequest;
import com.dani.mmk.backend.model.Player;
import com.dani.mmk.backend.model.User;
import com.dani.mmk.backend.model.UserRole;
import com.dani.mmk.backend.repository.MatchmakingRequestRepository;
import com.dani.mmk.backend.repository.PlayerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTests {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private UserService userService;

    @Mock
    private MatchmakingRequestRepository matchmakingRequestRepository;

    @InjectMocks
    private PlayerService playerService;

    @Test
    void getPlayerById_returnsPlayer_whenExists() {
        User user = createUser("john@example.com", "john", "hash", UserRole.PLAYER, true);
        Player player = new Player(user);

        when(playerRepository.findById(10L)).thenReturn(Optional.of(player));

        Player result = playerService.getPlayerById(10L);

        assertSame(player, result);
    }

    @Test
    void getPlayerById_throwsNotFound_whenMissing() {
        when(playerRepository.findById(10L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> playerService.getPlayerById(10L));

        assertTrue(ex.getMessage().contains("10"));
    }

    @Test
    void getPlayerByUserId_returnsPlayer_whenExists() {
        User user = createUser("john@example.com", "john", "hash", UserRole.PLAYER, true);
        Player player = new Player(user);

        when(playerRepository.findByUserId(1L)).thenReturn(Optional.of(player));

        Player result = playerService.getPlayerByUserId(1L);

        assertSame(player, result);
    }

    @Test
    void getPlayerByUserId_throwsNotFound_whenMissing() {
        when(playerRepository.findByUserId(99L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> playerService.getPlayerByUserId(99L));

        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    void createPlayer_createsPlayer_whenUserIsActiveAndRolePlayer() {
        User user = createUser("p@example.com", "player", "hash", UserRole.PLAYER, true);

        when(userService.getUserById(3L)).thenReturn(user);
        when(playerRepository.findByUserId(3L)).thenReturn(Optional.empty());
        when(playerRepository.save(any(Player.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Player result = playerService.createPlayer(3L);

        assertNotNull(result);
        assertSame(user, result.getUser());
    }

    @Test
    void createPlayer_throwsWhenUserInactive() {
        User user = createUser("inactive@example.com", "inactive", "hash", UserRole.PLAYER, false);
        when(userService.getUserById(4L)).thenReturn(user);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> playerService.createPlayer(4L));

        assertTrue(ex.getMessage().toLowerCase().contains("inactive"));
    }

    @Test
    void createPlayer_throwsWhenRoleIsNotPlayer() {
        User user = createUser("admin@example.com", "admin", "hash", UserRole.ADMIN, true);
        when(userService.getUserById(5L)).thenReturn(user);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> playerService.createPlayer(5L));

        assertTrue(ex.getMessage().contains("non-PLAYER"));
    }

    @Test
    void createPlayer_throwsWhenPlayerAlreadyExists() {
        User user = createUser("dup@example.com", "dup", "hash", UserRole.PLAYER, true);
        Player existingPlayer = new Player(user);

        when(userService.getUserById(6L)).thenReturn(user);
        when(playerRepository.findByUserId(6L)).thenReturn(Optional.of(existingPlayer));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> playerService.createPlayer(6L));

        assertTrue(ex.getMessage().toLowerCase().contains("already exists"));
    }

    @Test
    void getPlayerMatchmakingRequests_returnsRequests_whenPlayerExists() {
        User user = createUser("mmr@example.com", "player_mmr", "hash", UserRole.PLAYER, true);
        Player player = new Player(user);
        MatchmakingRequest req1 = org.mockito.Mockito.mock(MatchmakingRequest.class);
        MatchmakingRequest req2 = org.mockito.Mockito.mock(MatchmakingRequest.class);
        List<MatchmakingRequest> requests = Arrays.asList(req1, req2);

        when(playerRepository.findById(7L)).thenReturn(Optional.of(player));
        when(matchmakingRequestRepository.findByPlayer(player)).thenReturn(requests);

        List<MatchmakingRequest> result = playerService.getPlayerMatchmakingRequests(7L);

        assertEquals(2, result.size());
        assertSame(req1, result.get(0));
        assertSame(req2, result.get(1));
    }

    @Test
    void getPlayerMatchmakingRequests_returnsEmptyList_whenPlayerHasNoRequests() {
        User user = createUser("empty@example.com", "player_empty", "hash", UserRole.PLAYER, true);
        Player player = new Player(user);

        when(playerRepository.findById(8L)).thenReturn(Optional.of(player));
        when(matchmakingRequestRepository.findByPlayer(player)).thenReturn(Collections.emptyList());

        List<MatchmakingRequest> result = playerService.getPlayerMatchmakingRequests(8L);

        assertTrue(result.isEmpty());
    }

    @Test
    void getPlayerMatchmakingRequests_throwsNotFound_whenPlayerDoesNotExist() {
        when(playerRepository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
            () -> playerService.getPlayerMatchmakingRequests(999L));

        assertTrue(ex.getMessage().contains("999"));
    }

    private static User createUser(String email, String username, String passwordHash, UserRole role, boolean active) {
        User user = new User(email, username, passwordHash, role, active);
        user.setActive(active);
        return user;
    }
}


