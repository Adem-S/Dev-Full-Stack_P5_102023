package com.openclassrooms.starterjwt.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;

public class SessionTest {

	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	void testSessionConstructor() {

		Long id = 1L;
		String name = "Session 1";
		Date date = new Date();
		String description = "Description";
		Teacher teacher = new Teacher();
		List<User> users = new ArrayList<>();
		LocalDateTime createdAt = LocalDateTime.now();
		LocalDateTime updatedAt = LocalDateTime.now();

		Session session = new Session();
		session.setId(id);
		session.setName(name);
		session.setDate(date);
		session.setDescription(description);
		session.setTeacher(teacher);
		session.setUsers(users);
		session.setCreatedAt(createdAt);
		session.setUpdatedAt(updatedAt);

		assertNotNull(session);
		assertThat(session.getId()).isEqualTo(id);
		assertThat(session.getName()).isEqualTo(name);
		assertThat(session.getDate()).isEqualTo(date);
		assertThat(session.getDescription()).isEqualTo(description);
		assertThat(session.getTeacher()).isEqualTo(teacher);
		assertThat(session.getUsers()).isEqualTo(users);
		assertThat(session.getCreatedAt()).isEqualTo(createdAt);
		assertThat(session.getUpdatedAt()).isEqualTo(updatedAt);
	}

	@Test
	public void testSessionBuilder() {

		Long id = 1L;
		String name = "Session 1";
		Date date = new Date();
		String description = "Description";
		Teacher teacher = new Teacher();
		List<User> users = new ArrayList<>();
		LocalDateTime createdAt = LocalDateTime.now();
		LocalDateTime updatedAt = LocalDateTime.now();

		Session session = Session.builder().id(id).name(name).date(date).description(description).teacher(teacher)
				.users(users).createdAt(createdAt).updatedAt(updatedAt).build();

		assertAll(() -> assertThat(session).isNotNull(), () -> assertThat(session.getId()).isEqualTo(id),
				() -> assertThat(session.getName()).isEqualTo(name),
				() -> assertThat(session.getDate()).isEqualTo(date),
				() -> assertThat(session.getDescription()).isEqualTo(description),
				() -> assertThat(session.getTeacher()).isEqualTo(teacher),
				() -> assertThat(session.getUsers()).isEqualTo(users),
				() -> assertThat(session.getCreatedAt()).isEqualTo(createdAt),
				() -> assertThat(session.getUpdatedAt()).isEqualTo(updatedAt));
	}

	@Test
	public void testValidationAnnotations() {

		Session session = new Session();
		session.setName("testtesttesttesttesttesttesttestesttesttesttesttesttesttesttest");

		Set<ConstraintViolation<Session>> violations = validator.validate(session);
		assertFalse(violations.isEmpty());
	}

	@Test
	public void testEquals() {

		Session session1 = new Session(1L, "Session1", new Date(), "Description1", new Teacher(), new ArrayList<>(),
				LocalDateTime.now(), LocalDateTime.now());
		Session session2 = new Session(1L, "Session1", new Date(), "Description1", new Teacher(), new ArrayList<>(),
				LocalDateTime.now(), LocalDateTime.now());
		Session session3 = new Session(2L, "Session2", new Date(), "Description2", new Teacher(), new ArrayList<>(),
				LocalDateTime.now(), LocalDateTime.now());

		// Test equals
		assertThat(session1).isEqualTo(session2);
		assertThat(session1).isNotEqualTo(session3);

		// Test hashCode
		assertThat(session1.hashCode()).isEqualTo(session2.hashCode());
		assertThat(session1.hashCode()).isNotEqualTo(session3.hashCode());
	}

	@Test
	void testSessionToString() {
		Session session = new Session(1L, "Session1", new Date(), "Description1", new Teacher(), new ArrayList<>(),
				null, null);
		String expectedToString = "Session(id=" + session.getId() + ", name=" + session.getName() + ", date="
				+ session.getDate() + ", description=" + session.getDescription() + ", teacher=" + session.getTeacher()
				+ ", users=" + session.getUsers() + ", createdAt=null, updatedAt=null)";

		assertThat(session.toString()).isEqualTo(expectedToString);
	}
}
