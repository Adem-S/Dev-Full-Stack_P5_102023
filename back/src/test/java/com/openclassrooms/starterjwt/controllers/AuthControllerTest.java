package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private JwtUtils jwtUtils;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private UserRepository userRepository;

	private AuthController authController;

	@BeforeEach
	void setUp() {
		authController = new AuthController(authenticationManager, passwordEncoder, jwtUtils, userRepository);
	}

	@Test
	public void testAuthenticateUser() {
		Long id = 1L;
		String email = "test@gmail.com";
		String password = "password";
		String firstname = "firstname";
		String lastname = "lastname";
		boolean isAdmin = false;

		UserDetailsImpl userDetails = UserDetailsImpl.builder().username(email).firstName(firstname).lastName(lastname)
				.id(id).password(password).build();

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

		when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password)))
				.thenReturn(authentication);
		when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt");

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(User.builder().id(id).email(email)
				.password(password).firstName(firstname).lastName(lastname).admin(isAdmin).build()));

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail(email);
		loginRequest.setPassword(password);

		ResponseEntity<?> response = authController.authenticateUser(loginRequest);
		JwtResponse responseBody = (JwtResponse) response.getBody();

		assertAll(() -> assertEquals(HttpStatus.OK, response.getStatusCode()),
				() -> assertEquals(email, responseBody.getUsername()),
				() -> assertEquals(firstname, responseBody.getFirstName()),
				() -> assertEquals(lastname, responseBody.getLastName()), () -> assertEquals(id, responseBody.getId()),
				() -> assertEquals(isAdmin, responseBody.getAdmin()),
				() -> assertEquals("Bearer", responseBody.getType()), () -> assertNotNull(responseBody.getToken()));
	}

	@Test
	public void testRegisterUser() {
		String email = "test@gmail.com";
		String password = "password";

		when(userRepository.existsByEmail(email)).thenReturn(false);
		when(passwordEncoder.encode(password)).thenReturn("hashed");
		when(userRepository.save(any(User.class))).thenReturn(new User());

		SignupRequest signupRequest = new SignupRequest();
		signupRequest.setEmail(email);
		signupRequest.setFirstName("firstname");
		signupRequest.setLastName("lastname");
		signupRequest.setPassword(password);

		ResponseEntity<?> response = authController.registerUser(signupRequest);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testRegisterUserEmailAlreadyTaken() {
		String email = "test@gmail.com";
		String password = "password";

		when(userRepository.existsByEmail(email)).thenReturn(true);

		SignupRequest signupRequest = new SignupRequest();
		signupRequest.setEmail(email);
		signupRequest.setFirstName("firstname");
		signupRequest.setLastName("lastname");
		signupRequest.setPassword(password);

		ResponseEntity<?> response = authController.registerUser(signupRequest);

		MessageResponse messageResponse = (MessageResponse) response.getBody();

		assertAll(() -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
				() -> assertEquals("Error: Email is already taken!", messageResponse.getMessage()));
	}
}