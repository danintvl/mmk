package com.dani.mmk.backend.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTests {

	@Test
	void shouldHaveNullFieldsWithNoArgsConstructor() {
		Player player = new Player();

		assertNull(player.getId());
		assertNull(player.getUser());
	}

	@Test
	void shouldCreatePlayerWithUserConstructor() {
		User user = new User(
				"test@example.com",
				"testuser",
				"hashedPassword",
				UserRole.PLAYER,
				true
		);

		Player player = new Player(user);

		assertNull(player.getId());
		assertSame(user, player.getUser());
	}

	@Test
	void shouldSetAndGetUser() {
		Player player = new Player();
		User user = new User(
				"john@example.com",
				"john",
				"hash",
				UserRole.ADMIN,
				true
		);

		player.setUser(user);

		assertSame(user, player.getUser());
	}

	@Test
	void shouldAllowClearingUser() {
		User user = new User(
				"john@example.com",
				"john",
				"hash",
				UserRole.ADMIN,
				true
		);
		Player player = new Player(user);

		player.setUser(null);

		assertNull(player.getUser());
	}
}

