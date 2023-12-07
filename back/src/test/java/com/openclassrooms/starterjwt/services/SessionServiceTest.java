package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

	@Mock
	private SessionRepository sessionRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private SessionService sessionService;

	private Session session;

	@BeforeEach
	void setUp() {
		session = new Session(1L, "Session", new Date(), "Description", null, new ArrayList<>(), null, null);

	}

	@Test
	public void testCreateSession() {

		when(sessionRepository.save(session)).thenReturn(session);

		Session result = sessionService.create(session);

		verify(sessionRepository, times(1)).save(session);
		assertEquals(session, result);
	}

	@Test
	public void testDeleteSession() {

		Long sessionId = 1L;

		doNothing().when(sessionRepository).deleteById(sessionId);

		sessionService.delete(sessionId);

		verify(sessionRepository, times(1)).deleteById(sessionId);
	}

	@Test
	public void testFindAllSession() {

		List<Session> sessions = Arrays.asList(session, session);

		when(sessionRepository.findAll()).thenReturn(sessions);

		List<Session> result = sessionService.findAll();

		verify(sessionRepository, times(1)).findAll();
		assertEquals(sessions, result);
	}

	@Test
	public void testGetSessionById() {

		Long sessionId = 1L;

		when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

		Session result = sessionService.getById(sessionId);

		verify(sessionRepository, times(1)).findById(sessionId);
		assertEquals(session, result);
	}

	@Test
	public void testUpdateSession() {

		Long sessionId = 1L;

		when(sessionRepository.save(session)).thenReturn(session);

		Session result = sessionService.update(sessionId, session);

		verify(sessionRepository, times(1)).save(session);
		assertEquals(session, result);
	}

	@Test
	public void testParticipateSession() {

		Long sessionId = 1L;
		Long userId = 2L;

		User user = new User(userId, "test@gmail.com", "lastname", "firstname", "password", true, null, null);

		when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		sessionService.participate(sessionId, userId);

		verify(sessionRepository, times(1)).findById(sessionId);
		verify(userRepository, times(1)).findById(userId);

		assertTrue(session.getUsers().contains(user));
	}

	@Test
	public void testParticipateSessionBadRequest() {

		Long sessionId = 1L;
		Long userId = 2L;

		User user = new User(userId, "test@gmail.com", "lastname", "firstname", "password", true, null, null);

		session.getUsers().add(user);

		when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		assertThrows(BadRequestException.class, () -> sessionService.participate(sessionId, userId));
	}

	@Test
	public void testParticipateSessionNotFoundSession() {

		Long sessionId = 1L;
		Long userId = 2L;

		when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));
	}

	@Test
	public void testParticipateSessionNotFoundUser() {

		Long sessionId = 1L;
		Long userId = 2L;

		when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));
	}

	@Test
	public void testNoLongerParticipateSession() {

		Long sessionId = 1L;
		Long userId = 2L;

		User user = new User(userId, "test@gmail.com", "lastname", "firstname", "password", true, null, null);
		session.getUsers().add(user);

		when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

		sessionService.noLongerParticipate(sessionId, userId);

		verify(sessionRepository, times(1)).findById(sessionId);
		assertFalse(session.getUsers().contains(user));
	}

	@Test
	void testNoLongerParticipateInSessionBadRequestException() {

		session.setUsers(new ArrayList<>());

		when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

		assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L, 2L));

		verify(sessionRepository, never()).save(any());
	}

	@Test
	public void testNoLongerParticipateSessionNotFound() {

		Long sessionId = 1L;
		Long userId = 2L;

		when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(sessionId, userId));

		verify(sessionRepository, never()).save(any());
	}

}
