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

public class TeacherTest {

	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	void testTeacherConstructor() {

		Long id = 1L;
		String firstName = "firstname";
		String lastName = "lastname";
		LocalDateTime createdAt = LocalDateTime.now();
		LocalDateTime updatedAt = LocalDateTime.now();

		Teacher teacher = new Teacher();
		teacher.setId(id);
		teacher.setLastName(lastName);
		teacher.setFirstName(firstName);
		teacher.setCreatedAt(createdAt);
		teacher.setUpdatedAt(updatedAt);

		assertNotNull(teacher);
		assertThat(teacher.getId()).isEqualTo(id);
		assertThat(teacher.getLastName()).isEqualTo(lastName);
		assertThat(teacher.getFirstName()).isEqualTo(firstName);
		assertThat(teacher.getCreatedAt()).isEqualTo(createdAt);
		assertThat(teacher.getUpdatedAt()).isEqualTo(updatedAt);
	}

	@Test
	public void testTeacherBuilder() {

		Long id = 1L;
		String lastName = "lastname";
		String firstName = "firstname";
		LocalDateTime createdAt = LocalDateTime.now();
		LocalDateTime updatedAt = LocalDateTime.now();

		Teacher teacher = Teacher.builder().id(id).lastName(lastName).firstName(firstName).createdAt(createdAt)
				.updatedAt(updatedAt).build();

		assertAll(() -> assertThat(teacher).isNotNull(), () -> assertThat(teacher.getId()).isEqualTo(id),
				() -> assertThat(teacher.getLastName()).isEqualTo(lastName),
				() -> assertThat(teacher.getFirstName()).isEqualTo(firstName),
				() -> assertThat(teacher.getCreatedAt()).isEqualTo(createdAt),
				() -> assertThat(teacher.getUpdatedAt()).isEqualTo(updatedAt));
	}

	@Test
	public void testValidationAnnotations() {

		Teacher teacher = new Teacher();
		teacher.setFirstName("testtesttesttesttesttesttesttestesttesttesttesttesttesttesttest");

		Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
		assertFalse(violations.isEmpty());

	}

	@Test
	public void testEquals() {
		Teacher teacher1 = new Teacher(1L, "lastname", "firstname", LocalDateTime.now(), LocalDateTime.now());
		Teacher teacher2 = new Teacher(1L, "lastname", "firstname", LocalDateTime.now(), LocalDateTime.now());
		Teacher teacher3 = new Teacher(2L, "lastname2", "firstname2", LocalDateTime.now(), LocalDateTime.now());

		// Test equals
		assertThat(teacher1).isEqualTo(teacher2);
		assertThat(teacher1).isNotEqualTo(teacher3);

		// Test hashCode
		assertThat(teacher1.hashCode()).isEqualTo(teacher2.hashCode());
		assertThat(teacher1.hashCode()).isNotEqualTo(teacher3.hashCode());
	}

	@Test
	void testTeacherToString() {
		Teacher teacher = new Teacher(1L, "lastname", "firstname", null, null);
		String expectedToString = "Teacher(id=1, lastName=lastname, firstName=firstname, createdAt=null, updatedAt=null)";

		assertThat(teacher.toString()).isEqualTo(expectedToString);
	}
}