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

class MatchmakingRequestTests {

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
	void shouldHaveDefaultCollectionsWithProtectedNoArgsConstructor() {
		MatchmakingRequest request = new MatchmakingRequest();

		assertNull(request.getId());
		assertNull(request.getPlayer());
		assertNotNull(request.getAvailabilitySlots());
		assertTrue(request.getAvailabilitySlots().isEmpty());
		assertNull(request.getAcceptableFields());
		assertNull(request.getMatchedMatch());
		assertNull(request.getStatus());
		assertNull(request.getCreatedAt());
	}

	@Test
	void shouldSetAndGetFields() {
		MatchmakingRequest request = new MatchmakingRequest();
		Player player = new Player(new User("test@example.com", "testuser", "hash", UserRole.PLAYER, true));
				Match match = new Match(LocalDateTime.of(2026, 4, 2, 18, 30), MatchStatus.CREATED);
		Field field = new Field("Central Court", "Paris");

		request.setPlayer(player);
		request.setStatus(MatchmakingRequestStatus.QUEUED);
		request.setAcceptableFields(List.of(field));
		request.setMatchedMatch(match);
		request.setCreatedAt(LocalDateTime.of(2026, 4, 2, 18, 0));

		assertSame(player, request.getPlayer());
		assertEquals(MatchmakingRequestStatus.QUEUED, request.getStatus());
		assertEquals(List.of(field), request.getAcceptableFields());
		assertSame(match, request.getMatchedMatch());
		assertEquals(LocalDateTime.of(2026, 4, 2, 18, 0), request.getCreatedAt());
	}

	@Test
	void shouldAddAndRemoveAvailabilitySlotsWithBackReference() {
		MatchmakingRequest request = new MatchmakingRequest();
		AvailabilitySlot slot = new AvailabilitySlot(
				LocalDateTime.of(2026, 4, 2, 19, 0),
				LocalDateTime.of(2026, 4, 2, 20, 0),
				null
		);

		request.addAvailabilitySlot(slot);

		assertEquals(1, request.getAvailabilitySlots().size());
		assertSame(request, slot.getMatchmakingRequest());

		request.removeAvailabilitySlot(slot);

		assertTrue(request.getAvailabilitySlots().isEmpty());
		assertNull(slot.getMatchmakingRequest());
	}

	@Test
	void shouldReplaceAvailabilitySlotsAndClearOldBackReferences() {
		MatchmakingRequest request = new MatchmakingRequest();
		AvailabilitySlot oldSlot = new AvailabilitySlot(
				LocalDateTime.of(2026, 4, 2, 19, 0),
				LocalDateTime.of(2026, 4, 2, 20, 0),
				null
		);
		AvailabilitySlot newSlot = new AvailabilitySlot(
				LocalDateTime.of(2026, 4, 2, 20, 0),
				LocalDateTime.of(2026, 4, 2, 21, 0),
				null
		);

		request.addAvailabilitySlot(oldSlot);
		request.setAvailabilitySlots(List.of(newSlot));

		assertEquals(1, request.getAvailabilitySlots().size());
		assertSame(request, newSlot.getMatchmakingRequest());
		assertNull(oldSlot.getMatchmakingRequest());
	}

	@Test
	void shouldClearAvailabilitySlotsWhenSetAvailabilitySlotsIsNull() {
		MatchmakingRequest request = new MatchmakingRequest();
		AvailabilitySlot slot = new AvailabilitySlot(
				LocalDateTime.of(2026, 4, 2, 19, 0),
				LocalDateTime.of(2026, 4, 2, 20, 0),
				null
		);

		request.addAvailabilitySlot(slot);
		request.setAvailabilitySlots(null);

		assertTrue(request.getAvailabilitySlots().isEmpty());
		assertNull(slot.getMatchmakingRequest());
	}

	@Test
	void shouldBeValidWhenRequestIsCorrect() {
		MatchmakingRequest request = new MatchmakingRequest();
		request.setPlayer(new Player(new User("test@example.com", "testuser", "hash", UserRole.PLAYER, true)));
		request.setStatus(MatchmakingRequestStatus.QUEUED);
		request.setCreatedAt(LocalDateTime.of(2026, 4, 2, 18, 0));
		request.setAcceptableFields(List.of(new Field("Central Court", "Paris")));
		request.addAvailabilitySlot(new AvailabilitySlot(
				LocalDateTime.of(2026, 4, 2, 19, 0),
				LocalDateTime.of(2026, 4, 2, 20, 0),
				null
		));

		Set<ConstraintViolation<MatchmakingRequest>> violations = validator.validate(request);

		assertTrue(violations.isEmpty());
	}

	@Test
	void shouldBeInvalidWhenStatusIsNull() {
		MatchmakingRequest request = new MatchmakingRequest();
		request.setPlayer(new Player(new User("test@example.com", "testuser", "hash", UserRole.PLAYER, true)));
		request.setCreatedAt(LocalDateTime.of(2026, 4, 2, 18, 0));
		request.setAcceptableFields(List.of(new Field("Central Court", "Paris")));
		request.addAvailabilitySlot(new AvailabilitySlot(
				LocalDateTime.of(2026, 4, 2, 19, 0),
				LocalDateTime.of(2026, 4, 2, 20, 0),
				null
		));

		Set<ConstraintViolation<MatchmakingRequest>> violations = validator.validate(request);

		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("status")));
	}

	@Test
	void shouldBeInvalidWhenCreatedAtIsNull() {
		MatchmakingRequest request = new MatchmakingRequest();
		request.setPlayer(new Player(new User("test@example.com", "testuser", "hash", UserRole.PLAYER, true)));
		request.setStatus(MatchmakingRequestStatus.QUEUED);
		request.setAcceptableFields(List.of(new Field("Central Court", "Paris")));
		request.addAvailabilitySlot(new AvailabilitySlot(
				LocalDateTime.of(2026, 4, 2, 19, 0),
				LocalDateTime.of(2026, 4, 2, 20, 0),
				null
		));

		Set<ConstraintViolation<MatchmakingRequest>> violations = validator.validate(request);

		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("createdAt")));
	}

	@Test
	void shouldBeInvalidWhenAcceptableFieldsAreEmpty() {
		MatchmakingRequest request = new MatchmakingRequest();
		request.setPlayer(new Player(new User("test@example.com", "testuser", "hash", UserRole.PLAYER, true)));
		request.setStatus(MatchmakingRequestStatus.QUEUED);
		request.setCreatedAt(LocalDateTime.of(2026, 4, 2, 18, 0));
		request.setAcceptableFields(List.of());
		request.addAvailabilitySlot(new AvailabilitySlot(
				LocalDateTime.of(2026, 4, 2, 19, 0),
				LocalDateTime.of(2026, 4, 2, 20, 0),
				null
		));

		Set<ConstraintViolation<MatchmakingRequest>> violations = validator.validate(request);

		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("acceptableFields")));
	}

	@Test
	void shouldBeInvalidWhenAvailabilitySlotsAreEmpty() {
		MatchmakingRequest request = new MatchmakingRequest();
		request.setPlayer(new Player(new User("test@example.com", "testuser", "hash", UserRole.PLAYER, true)));
		request.setStatus(MatchmakingRequestStatus.QUEUED);
		request.setCreatedAt(LocalDateTime.of(2026, 4, 2, 18, 0));
		request.setAcceptableFields(List.of(new Field("Central Court", "Paris")));

		Set<ConstraintViolation<MatchmakingRequest>> violations = validator.validate(request);

		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("availabilitySlots")));
	}
}

