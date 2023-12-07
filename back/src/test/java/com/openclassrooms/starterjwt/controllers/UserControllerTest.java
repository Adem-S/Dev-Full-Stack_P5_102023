package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
	@Mock
	private UserMapper userMapper;
	@Mock
	private UserService userService;

	private UserController userController;

	@BeforeEach
	void setUp() {
		userController = new UserController(userService, userMapper);
	}

	@Test
	public void testFindUserById() {

		Long id = 1L;
		User user = User.builder().id(id).email("test@gmail.com").firstName("firstName").lastName("lastName")
				.password("password").admin(false).build();

		UserDto userDto = new UserDto();
		userDto.setId(id);
		userDto.setEmail(user.getEmail());
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		userDto.setPassword(user.getPassword());
		userDto.setAdmin(user.isAdmin());

		when(userService.findById(id)).thenReturn(user);
		when(userMapper.toDto(user)).thenReturn(userDto);

		ResponseEntity<?> response = userController.findById(id.toString());
		UserDto responseBody = (UserDto) response.getBody();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(userDto, responseBody);
	}

	@Test
	public void testFindUserByIdNotFound() {
		Long id = 1L;

		when(userService.findById(id)).thenReturn(null);

		ResponseEntity<?> response = userController.findById(id.toString());

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	public void testFindUserByIdBadRequest() {

		ResponseEntity<?> response = userController.findById("");

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	public void testDeleteUser() {
		Long id = 1L;

		User user = User.builder().id(id).email("test@gmail.com").firstName("firstName").lastName("lastName")
				.password("password").admin(true).build();

		UserDetailsImpl userDetails = UserDetailsImpl.builder().id(id).username(user.getEmail())
				.firstName(user.getFirstName()).lastName(user.getLastName()).password(user.getPassword())
				.admin(user.isAdmin()).build();

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

		when(userService.findById(id)).thenReturn(user);

		SecurityContext securityContext = mock(SecurityContext.class);
		SecurityContextHolder.setContext(securityContext);

		when(securityContext.getAuthentication()).thenReturn(authentication);
		doNothing().when(userService).delete(id);

		ResponseEntity<?> response = userController.save("" + id);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testDeleteUserUnauthorized() {
		Long id = 1L;

		String otherMail = "test2@gmail.com";

		User user = User.builder().id(id).email("test@gmail.com").firstName("firstName").lastName("lastName")
				.password("password").admin(true).build();

		UserDetailsImpl userDetails = UserDetailsImpl.builder().id(id).username(otherMail)
				.firstName(user.getFirstName()).lastName(user.getLastName()).password(user.getPassword())
				.admin(user.isAdmin()).build();

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

		when(userService.findById(id)).thenReturn(user);

		SecurityContext securityContext = mock(SecurityContext.class);
		SecurityContextHolder.setContext(securityContext);

		when(securityContext.getAuthentication()).thenReturn(authentication);

		ResponseEntity<?> response = userController.save("" + id);

		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	public void testDeleteUserNotFound() {
		Long id = 1L;

		when(userService.findById(id)).thenReturn(null);

		ResponseEntity<?> response = userController.save("" + id);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	public void testDeleteUserBadRequest() {

		ResponseEntity<?> response = userController.save("");

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNull(response.getBody());
	}
}
