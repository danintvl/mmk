package com.dani.mmk.backend.service;

import com.dani.mmk.backend.model.User;
import com.dani.mmk.backend.model.UserRole;
import com.dani.mmk.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserById_returnsUser_whenExists() {
        User existingUser = createUser("john@example.com", "john", "hash", UserRole.PLAYER, true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        User result = userService.getUserById(1L);

        assertSame(existingUser, result);
    }

    @Test
    void getUserById_throwsNotFound_whenMissing() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> userService.getUserById(99L)
        );

        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    void findByEmail_returnsUser_whenExists() {
        User existingUser = createUser("john@example.com", "john", "hash", UserRole.PLAYER, true);
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(existingUser));

        Optional<User> result = userService.findByEmail("john@example.com");

        assertTrue(result.isPresent());
        assertSame(existingUser, result.get());
    }

    @Test
    void findByEmail_returnsEmpty_whenMissing() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        Optional<User> result = userService.findByEmail("missing@example.com");

        assertTrue(result.isEmpty());
    }

    @Test
    void findByUsername_returnsUser_whenExists() {
        User existingUser = createUser("john@example.com", "john", "hash", UserRole.PLAYER, true);
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(existingUser));

        Optional<User> result = userService.findByUsername("john");

        assertTrue(result.isPresent());
        assertSame(existingUser, result.get());
    }

    @Test
    void findByUsername_returnsEmpty_whenMissing() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        Optional<User> result = userService.findByUsername("ghost");

        assertTrue(result.isEmpty());
    }

    @Test
    void findByRole_returnsUsers_forRequestedRole() {
        List<User> players = List.of(
                createUser("a@example.com", "a", "hashA", UserRole.PLAYER, true),
                createUser("b@example.com", "b", "hashB", UserRole.PLAYER, true)
        );
        when(userRepository.findByRole(UserRole.PLAYER)).thenReturn(players);

        List<User> result = userService.findByRole(UserRole.PLAYER);

        assertEquals(2, result.size());
        assertSame(players, result);
    }

    @Test
    void findByRole_returnsEmpty_whenNoUserHasRole() {
        when(userRepository.findByRole(UserRole.ADMIN)).thenReturn(List.of());

        List<User> result = userService.findByRole(UserRole.ADMIN);

        assertTrue(result.isEmpty());
    }

    @Test
    void updateUser_updatesAllowedFields_whenPayloadIsValid() {
        User existingUser = createUser("old@example.com", "oldUser", "oldHash", UserRole.PLAYER, true);
        User updatedUser = createUser("new@example.com", "newUser", "newHash", UserRole.ADMIN, true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User result = userService.updateUser(1L, updatedUser);

        assertEquals("new@example.com", result.getEmail());
        assertEquals("newUser", result.getUsername());
        assertEquals("newHash", result.getPasswordHash());
        assertEquals(UserRole.ADMIN, result.getRole());
        assertTrue(result.isActive());
    }

    @Test
    void updateUser_keepsExistingValues_whenPayloadFieldsAreNull() {
        User existingUser = createUser("same@example.com", "sameUser", "oldHash", UserRole.PLAYER, true);
        User updatedUser = new User();

        when(userRepository.findById(2L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User result = userService.updateUser(2L, updatedUser);

        assertEquals("same@example.com", result.getEmail());
        assertEquals("sameUser", result.getUsername());
        assertEquals("oldHash", result.getPasswordHash());
        assertEquals(UserRole.PLAYER, result.getRole());
        assertTrue(result.isActive());
    }

    @Test
    void updateUser_ignoresBlankPasswordHash() {
        User existingUser = createUser("old@example.com", "oldUser", "oldHash", UserRole.PLAYER, true);
        User updatedUser = createUser("old@example.com", "oldUser", "   ", null, true);

        when(userRepository.findById(5L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User result = userService.updateUser(5L, updatedUser);

        assertEquals("oldHash", result.getPasswordHash());
    }

    @Test
    void updateUser_rejectsDuplicateEmail() {
        User existingUser = createUser("old@example.com", "oldUser", "oldHash", UserRole.PLAYER, true);
        User updatedUser = createUser("taken@example.com", "oldUser", "oldHash", UserRole.PLAYER, true);

        when(userRepository.findById(3L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail("taken@example.com")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUser(3L, updatedUser)
        );

        assertTrue(ex.getMessage().toLowerCase().contains("email"));
    }

    @Test
    void updateUser_rejectsDuplicateUsername() {
        User existingUser = createUser("old@example.com", "oldUser", "oldHash", UserRole.PLAYER, true);
        User conflictingUser = createUser("other@example.com", "takenUser", "hash", UserRole.PLAYER, true);
        User updatedUser = createUser("old@example.com", "takenUser", "oldHash", UserRole.PLAYER, true);

        when(userRepository.findById(4L)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUsername("takenUser")).thenReturn(Optional.of(conflictingUser));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUser(4L, updatedUser)
        );

        assertTrue(ex.getMessage().toLowerCase().contains("username"));
    }

    @Test
    void updateUser_throwsNotFound_whenUserDoesNotExist() {
        User updatedUser = createUser("new@example.com", "newUser", "newHash", UserRole.PLAYER, true);
        when(userRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(404L, updatedUser));
    }

    @Test
    void updateUser_rejectsNullPayload() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUser(6L, null)
        );

        assertTrue(ex.getMessage().contains("updatedUser"));
    }

    @Test
    void deactivateUser_setsUserInactive() {
        User existingUser = createUser("john@example.com", "john", "hash", UserRole.PLAYER, true);
        when(userRepository.findById(7L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User result = userService.deactivateUser(7L);

        assertFalse(result.isActive());
    }

    @Test
    void deactivateUser_isIdempotent_whenUserAlreadyInactive() {
        User existingUser = createUser("john@example.com", "john", "hash", UserRole.PLAYER, false);
        when(userRepository.findById(9L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User result = userService.deactivateUser(9L);

        assertFalse(result.isActive());
    }

    @Test
    void deactivateUser_throwsNotFound_whenUserMissing() {
        when(userRepository.findById(8L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.deactivateUser(8L));
    }

    private static User createUser(String email, String username, String passwordHash, UserRole role, boolean active) {
        return new User(email, username, passwordHash, role, active);
    }
}
