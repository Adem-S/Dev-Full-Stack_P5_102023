package com.openclassrooms.starterjwt.payload.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.openclassrooms.starterjwt.controllers.TeacherController;

public class SignupRequestTest {

	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	SignupRequest signupRequest1;

	SignupRequest signupRequest2;

	@BeforeEach
	void setUp() {
		signupRequest1 = new SignupRequest();
		signupRequest1.setEmail("test@example.com");
		signupRequest1.setFirstName("firstname");
		signupRequest1.setLastName("lastname");
		signupRequest1.setPassword("password");

		signupRequest2 = new SignupRequest();
		signupRequest2.setEmail("test@example.com");
		signupRequest2.setFirstName("firstname");
		signupRequest2.setLastName("lastname");
		signupRequest2.setPassword("password");
	}

	@Test
	void testSignupRequestValid() {

		Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest1);

		assertTrue(violations.isEmpty());
	}

	@Test
	void testEqual() {
		SignupRequest request1 = signupRequest1;
		SignupRequest request2 = signupRequest2;

		assertThat(request1).isEqualTo(request2);
	}

	@Test
	void testHashCodeEqual() {
		SignupRequest request1 = signupRequest1;
		SignupRequest request2 = signupRequest2;
		assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
	}

	@Test
	void testToString() {
		assertThat(signupRequest1.toString()).isNotBlank();
	}

}
