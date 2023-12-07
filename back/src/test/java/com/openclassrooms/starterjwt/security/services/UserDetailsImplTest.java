package com.openclassrooms.starterjwt.security.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.User;

@ExtendWith(MockitoExtension.class)
public class UserDetailsImplTest {

	@InjectMocks
	UserDetailsImpl userDetails;

	User user;

	@BeforeEach
	void setUp() {

		user = new User("test@gmail.com", "lastName", "firstName", "password", true);
		userDetails = UserDetailsImpl.builder().id(user.getId()).username(user.getEmail())
				.firstName(user.getFirstName()).lastName(user.getLastName()).admin(user.isAdmin())
				.password(user.getPassword()).build();

	}

	@Test
	void testGetUserDetailsProperties() {

		assertEquals(userDetails.getUsername(), user.getEmail());
		assertEquals(userDetails.getFirstName(), user.getFirstName());
		assertEquals(userDetails.getLastName(), user.getLastName());
		assertEquals(userDetails.getPassword(), user.getPassword());
		assertEquals(userDetails.getAdmin(), user.isAdmin());

	}

	@Test
	void testGetUserDetailsMethod() {

		assertEquals(userDetails.isAccountNonExpired(), true);
		assertEquals(userDetails.isAccountNonLocked(), true);
		assertEquals(userDetails.isCredentialsNonExpired(), true);
		assertEquals(userDetails.isEnabled(), true);

	}

	@Test
	void testEquals() {

		User user3 = new User("test3@gmail.com", "lastName3", "firstName3", "password3", true);
		UserDetailsImpl userDetails1 = userDetails;
		UserDetailsImpl userDetails2 = userDetails;
		UserDetailsImpl userDetails3 = UserDetailsImpl.builder().id(2L).username(user3.getEmail())
				.firstName(user3.getFirstName()).lastName(user3.getLastName()).admin(user3.isAdmin())
				.password(user3.getPassword()).build();

		assertTrue(userDetails1.equals(userDetails1));
		assertTrue(userDetails1.equals(userDetails2));
		assertFalse(userDetails1.equals(userDetails3));
		assertFalse(userDetails1.equals(null));
		assertFalse(userDetails1.equals("invalid"));
		
	}

};
