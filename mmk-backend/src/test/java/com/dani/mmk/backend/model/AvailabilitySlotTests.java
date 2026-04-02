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

class AvailabilitySlotTests {

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
		AvailabilitySlot slot = new AvailabilitySlot();

		assertNull(slot.getId());
		assertNull(slot.getStartAt());
		assertNull(slot.getEndAt());
		assertNull(slot.getMatchmakingRequest());
	}

	@Test
	void shouldCreateAvailabilitySlotWithConstructor() {
		LocalDateTime startAt = LocalDateTime.of(2026, 4, 2, 19, 0);
		LocalDateTime endAt = LocalDateTime.of(2026, 4, 2, 20, 0);
		MatchmakingRequest request = new MatchmakingRequest();

		AvailabilitySlot slot = new AvailabilitySlot(startAt, endAt, request);

		assertNull(slot.getId());
		assertEquals(startAt, slot.getStartAt());
		assertEquals(endAt, slot.getEndAt());
		assertSame(request, slot.getMatchmakingRequest());
	}

	@Test
	void shouldSetAndGetFields() {
		AvailabilitySlot slot = new AvailabilitySlot();
		LocalDateTime startAt = LocalDateTime.of(2026, 4, 2, 19, 0);
		LocalDateTime endAt = LocalDateTime.of(2026, 4, 2, 20, 0);
		MatchmakingRequest request = new MatchmakingRequest();

		slot.setStartAt(startAt);
		slot.setEndAt(endAt);
		slot.setMatchmakingRequest(request);

		assertEquals(startAt, slot.getStartAt());
		assertEquals(endAt, slot.getEndAt());
		assertSame(request, slot.getMatchmakingRequest());
	}

	@Test
	void shouldBeValidWhenRangeIsCorrect() {
		AvailabilitySlot slot = new AvailabilitySlot(
				LocalDateTime.of(2026, 4, 2, 19, 0),
				LocalDateTime.of(2026, 4, 2, 20, 0),
				new MatchmakingRequest()
		);

		Set<ConstraintViolation<AvailabilitySlot>> violations = validator.validate(slot);

		assertTrue(violations.isEmpty());
	}

	@Test
	void shouldBeInvalidWhenStartAtIsNull() {
		AvailabilitySlot slot = new AvailabilitySlot(null, LocalDateTime.of(2026, 4, 2, 20, 0), new MatchmakingRequest());

		Set<ConstraintViolation<AvailabilitySlot>> violations = validator.validate(slot);

		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("startAt")));
	}

	@Test
	void shouldBeInvalidWhenEndAtIsNull() {
		AvailabilitySlot slot = new AvailabilitySlot(LocalDateTime.of(2026, 4, 2, 19, 0), null, new MatchmakingRequest());

		Set<ConstraintViolation<AvailabilitySlot>> violations = validator.validate(slot);

		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("endAt")));
	}

	@Test
	void shouldBeInvalidWhenRangeIsNotCorrect() {
		AvailabilitySlot slot = new AvailabilitySlot(
				LocalDateTime.of(2026, 4, 2, 20, 0),
				LocalDateTime.of(2026, 4, 2, 19, 0),
				new MatchmakingRequest()
		);

		Set<ConstraintViolation<AvailabilitySlot>> violations = validator.validate(slot);

		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("validRange")));
	}

	@Test
	void shouldAcceptNullBoundsInRangeCheck() {
		AvailabilitySlot startNull = new AvailabilitySlot(null, LocalDateTime.of(2026, 4, 2, 20, 0), new MatchmakingRequest());
		AvailabilitySlot endNull = new AvailabilitySlot(LocalDateTime.of(2026, 4, 2, 19, 0), null, new MatchmakingRequest());

		assertTrue(startNull.isValidRange());
		assertTrue(endNull.isValidRange());
	}
}
