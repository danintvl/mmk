package com.dani.mmk.backend.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MatchParticipantTests {

	private ValidatorFactory factory;
	private Validator validator;

	@BeforeEach
	void setUp() {
		factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@AfterEach
	void tearDown() {
		if (factory != null) {
			factory.close();
		}
	}

	@Test
	void shouldHaveNullFieldsWithProtectedNoArgsConstructor() {
		MatchParticipant participant = new MatchParticipant();

		assertNull(participant.getId());
		assertNull(participant.getJoinedAt());
		assertNull(participant.getPlayer());
		assertNull(participant.getMatch());
	}

	@Test
	void shouldCreateMatchParticipantWithConstructor() {
		LocalDateTime joinedAt = LocalDateTime.of(2026, 4, 2, 18, 35);
		Player player = new Player(new User("test@example.com", "testuser", "hash", UserRole.PLAYER, true));
		Match match = new Match(LocalDateTime.of(2026, 4, 2, 18, 30), MatchStatus.CREATED);

		MatchParticipant participant = new MatchParticipant(joinedAt, player, match);

		assertNull(participant.getId());
		assertEquals(joinedAt, participant.getJoinedAt());
		assertSame(player, participant.getPlayer());
		assertSame(match, participant.getMatch());
	}

	@Test
	void shouldSetAndGetFields() {
		MatchParticipant participant = new MatchParticipant();
		LocalDateTime joinedAt = LocalDateTime.of(2026, 4, 2, 18, 36);
		Player player = new Player(new User("test@example.com", "testuser", "hash", UserRole.PLAYER, true));
		Match match = new Match(LocalDateTime.of(2026, 4, 2, 18, 30), MatchStatus.CREATED);

		participant.setJoinedAt(joinedAt);
		participant.setPlayer(player);
		participant.setMatch(match);

		assertEquals(joinedAt, participant.getJoinedAt());
		assertSame(player, participant.getPlayer());
		assertSame(match, participant.getMatch());
	}

	@Test
	void shouldBeValidWhenJoinedAtIsPresent() {
		MatchParticipant participant = new MatchParticipant(
				LocalDateTime.of(2026, 4, 2, 18, 35),
				new Player(new User("test@example.com", "testuser", "hash", UserRole.PLAYER, true)),
								new Match(LocalDateTime.of(2026, 4, 2, 18, 30), MatchStatus.CREATED)
		);

		Set<ConstraintViolation<MatchParticipant>> violations = validator.validate(participant);

		assertTrue(violations.isEmpty());
	}

	@Test
	void shouldBeInvalidWhenJoinedAtIsNull() {
		MatchParticipant participant = new MatchParticipant(
				null,
				new Player(new User("test@example.com", "testuser", "hash", UserRole.PLAYER, true)),
								new Match(LocalDateTime.of(2026, 4, 2, 18, 30), MatchStatus.CREATED)
		);

		Set<ConstraintViolation<MatchParticipant>> violations = validator.validate(participant);

		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("joinedAt")));
	}
}

