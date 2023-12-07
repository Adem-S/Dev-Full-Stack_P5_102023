package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	@Test
	public void testDeleteUser() {

		Long userId = 1L;

		doNothing().when(userRepository).deleteById(userId);

		userService.delete(userId);

		verify(userRepository, times(1)).deleteById(userId);
	}

	@Test
	public void testFindUserById() {

		Long userId = 1L;
		User user = new User(userId, "test@gmail.com", "lastName", "firsttName", "password", false, null, null);

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		User result = userService.findById(userId);

		verify(userRepository, times(1)).findById(userId);
		assertEquals(user, result);
	}
}
