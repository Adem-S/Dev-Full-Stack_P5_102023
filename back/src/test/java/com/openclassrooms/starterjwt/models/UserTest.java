package com.openclassrooms.starterjwt.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;

public class UserTest {

	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	void testUserConstructor() {
		// Given
		Long id = 1L;
		String email = "test@example.com";
		String lastName = "lastName";
		String firstName = "firstName";
		String password = "password";
		boolean admin = true;
		LocalDateTime createdAt = LocalDateTime.now();
		LocalDateTime updatedAt = LocalDateTime.now();

		User user = new User();
		user.setId(id);
		user.setEmail(email);
		user.setLastName(lastName);
		user.setFirstName(firstName);
		user.setPassword(password);
		user.setAdmin(admin);
		user.setCreatedAt(createdAt);
		user.setUpdatedAt(updatedAt);

		assertAll(() -> assertNotNull(user), () -> assertThat(user.getId()).isEqualTo(id),
				() -> assertThat(user.getEmail()).isEqualTo(email),
				() -> assertThat(user.getLastName()).isEqualTo(lastName),
				() -> assertThat(user.getFirstName()).isEqualTo(firstName),
				() -> assertThat(user.getPassword()).isEqualTo(password),
				() -> assertThat(user.isAdmin()).isEqualTo(admin),
				() -> assertThat(user.getCreatedAt()).isEqualTo(createdAt),
				() -> assertThat(user.getUpdatedAt()).isEqualTo(updatedAt));
	}

	@Test
	public void testUserBuilder() {

		Long id = 1L;
		String email = "test@example.com";
		String lastName = "lastName";
		String firstName = "firstName";
		String password = "password";
		boolean admin = true;
		LocalDateTime createdAt = LocalDateTime.now();
		LocalDateTime updatedAt = LocalDateTime.now();

		User user = User.builder().id(id).email(email).lastName(lastName).firstName(firstName).password(password)
				.admin(admin).createdAt(createdAt).updatedAt(updatedAt).build();

		assertAll(() -> assertThat(user).isNotNull(), () -> assertThat(user.getId()).isEqualTo(id),
				() -> assertThat(user.getEmail()).isEqualTo(email),
				() -> assertThat(user.getLastName()).isEqualTo(lastName),
				() -> assertThat(user.getFirstName()).isEqualTo(firstName),
				() -> assertThat(user.getPassword()).isEqualTo(password),
				() -> assertThat(user.isAdmin()).isEqualTo(admin),
				() -> assertThat(user.getCreatedAt()).isEqualTo(createdAt),
				() -> assertThat(user.getUpdatedAt()).isEqualTo(updatedAt));
	}

	@Test
	public void testValidationAnnotations() {

		User user = new User();
		user.setEmail("invalidEmail");

		Set<ConstraintViolation<User>> violations = validator.validate(user);
		assertFalse(violations.isEmpty());
	}

	@Test
	public void testEquals() {
		User user1 = new User(1L, "test@example.com", "lastName", "firstName", "password", true, LocalDateTime.now(),
				LocalDateTime.now());
		User user2 = new User(1L, "test@example.com", "lastName", "firstName", "password", true, LocalDateTime.now(),
				LocalDateTime.now());
		User user3 = new User(2L, "test2@example.com", "lastName2", "firstName2", "password2", false,
				LocalDateTime.now(), LocalDateTime.now());

		// Test equals
		assertThat(user1).isEqualTo(user2);
		assertThat(user1).isNotEqualTo(user3);

		// Test hashCode
		assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
		assertThat(user1.hashCode()).isNotEqualTo(user3.hashCode());
	}

	@Test
	void tesUsertToString() {
		User user = new User(1L, "test@example.com", "lastName", "firstName", "password", true, null, null);
		String expectedToString = "User(id=1, email=test@example.com, lastName=lastName, firstName=firstName, password=password, admin=true, createdAt=null, updatedAt=null)";

		assertThat(user.toString()).isEqualTo(expectedToString);
	}
}
