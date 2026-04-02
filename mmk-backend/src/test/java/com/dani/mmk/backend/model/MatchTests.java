package com.dani.mmk.backend.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MatchTests {

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
	void shouldHaveDefaultParticipantsWithNoArgsConstructor() {
		Match match = new Match();

		assertNull(match.getId());
		assertNull(match.getScheduledAt());
		assertNull(match.getStatus());
		assertNotNull(match.getParticipants());
		assertTrue(match.getParticipants().isEmpty());
		assertNull(match.getField());
	}

	@Test
	void shouldCreateMatchWithConstructor() {
		LocalDateTime scheduledAt = LocalDateTime.of(2026, 4, 2, 18, 30);
		Match match = new Match(scheduledAt, MatchStatus.CREATED);

		assertNull(match.getId());
		assertEquals(scheduledAt, match.getScheduledAt());
		assertEquals(MatchStatus.CREATED, match.getStatus());
	}

	@Test
	void shouldSetAndGetFields() {
		Match match = new Match();
		LocalDateTime scheduledAt = LocalDateTime.of(2026, 4, 2, 19, 0);
		Field field = new Field("Central Court", "Paris");

		match.setScheduledAt(scheduledAt);
		match.setStatus(MatchStatus.CONFIRMED);
		match.setField(field);

		assertEquals(scheduledAt, match.getScheduledAt());
		assertEquals(MatchStatus.CONFIRMED, match.getStatus());
		assertSame(field, match.getField());
	}

	@Test
	void shouldBeValidWhenMatchIsCorrect() {
		Match match = new Match(
				LocalDateTime.of(2026, 4, 2, 18, 30),
				MatchStatus.CREATED
		);

		Set<ConstraintViolation<Match>> violations = validator.validate(match);

		assertTrue(violations.isEmpty());
	}

	@Test
	void shouldBeInvalidWhenScheduledAtIsNull() {
		Match match = new Match(null, MatchStatus.CREATED);

		Set<ConstraintViolation<Match>> violations = validator.validate(match);

		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("scheduledAt")));
	}

	@Test
	void shouldBeInvalidWhenStatusIsNull() {
		Match match = new Match(LocalDateTime.of(2026, 4, 2, 18, 30), null);

		Set<ConstraintViolation<Match>> violations = validator.validate(match);

		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("status")));
	}

	@Test
	void shouldAddAndRemoveParticipantsWithBackReference() {
		Match match = new Match(LocalDateTime.of(2026, 4, 2, 18, 30), MatchStatus.CREATED);
		Player player = new Player(new User("test@example.com", "testuser", "hash", UserRole.PLAYER, true));
		MatchParticipant participant = new MatchParticipant(LocalDateTime.of(2026, 4, 2, 18, 31), player, null);

		match.addParticipant(participant);

		assertEquals(1, match.getParticipants().size());
		assertSame(match, participant.getMatch());

		match.removeParticipant(participant);

		assertTrue(match.getParticipants().isEmpty());
		assertNull(participant.getMatch());
	}

	@Test
	void shouldReplaceParticipantsAndClearOldBackReferences() {
		Match match = new Match(LocalDateTime.of(2026, 4, 2, 18, 30), MatchStatus.CREATED);
		Player player1 = new Player(new User("one@example.com", "one", "hash", UserRole.PLAYER, true));
		Player player2 = new Player(new User("two@example.com", "two", "hash", UserRole.PLAYER, true));
		MatchParticipant oldParticipant = new MatchParticipant(LocalDateTime.of(2026, 4, 2, 18, 31), player1, null);
		MatchParticipant newParticipant = new MatchParticipant(LocalDateTime.of(2026, 4, 2, 18, 32), player2, null);

		match.addParticipant(oldParticipant);
		match.setParticipants(List.of(newParticipant));

		assertEquals(1, match.getParticipants().size());
		assertSame(match, newParticipant.getMatch());
		assertNull(oldParticipant.getMatch());
	}

	@Test
	void shouldClearParticipantsWhenSetParticipantsIsNull() {
		Match match = new Match(LocalDateTime.of(2026, 4, 2, 18, 30), MatchStatus.CREATED);
		MatchParticipant participant = new MatchParticipant(
				LocalDateTime.of(2026, 4, 2, 18, 31),
				new Player(new User("test@example.com", "testuser", "hash", UserRole.PLAYER, true)),
				null
		);

		match.addParticipant(participant);
		match.setParticipants(null);

		assertTrue(match.getParticipants().isEmpty());
		assertNull(participant.getMatch());
	}
}

