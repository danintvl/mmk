package com.dani.mmk.backend.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FieldTests {

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
	void shouldHaveNullFieldsWithNoArgsConstructor() {
		Field field = new Field();

		assertNull(field.getId());
		assertNull(field.getName());
		assertNull(field.getLocation());
	}

	@Test
	void shouldCreateFieldWithConstructor() {
		Field field = new Field("Central Court", "Paris");

		assertNull(field.getId());
		assertEquals("Central Court", field.getName());
		assertEquals("Paris", field.getLocation());
	}

	@Test
	void shouldSetAndGetFields() {
		Field field = new Field();

		field.setName("North Court");
		field.setLocation("Lyon");

		assertEquals("North Court", field.getName());
		assertEquals("Lyon", field.getLocation());
	}

	@Test
	void shouldBeValidWhenFieldIsCorrect() {
		Field field = new Field("Central Court", "Paris");

		Set<ConstraintViolation<Field>> violations = validator.validate(field);

		assertTrue(violations.isEmpty());
	}

	@Test
	void shouldBeInvalidWhenNameIsBlank() {
		Field field = new Field("", "Paris");

		Set<ConstraintViolation<Field>> violations = validator.validate(field);

		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
	}

	@Test
	void shouldBeInvalidWhenLocationIsBlank() {
		Field field = new Field("Central Court", "");

		Set<ConstraintViolation<Field>> violations = validator.validate(field);

		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("location")));
	}

	@Test
	void shouldBeInvalidWhenNameIsTooLong() {
		Field field = new Field("a".repeat(201), "Paris");

		Set<ConstraintViolation<Field>> violations = validator.validate(field);

		assertFalse(violations.isEmpty());
	}

	@Test
	void shouldBeInvalidWhenLocationIsTooLong() {
		Field field = new Field("Central Court", "a".repeat(201));

		Set<ConstraintViolation<Field>> violations = validator.validate(field);

		assertFalse(violations.isEmpty());
	}
}

