package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class SessionControllerTest {

	@Mock
	private SessionMapper sessionMapper;

	@Mock
	private SessionService sessionService;

	private SessionController sessionController;

	@BeforeEach
	void setUp() {
		sessionController = new SessionController(sessionService, sessionMapper);
	}

	@Test
	public void testFindSessionById() {

		Long id = 1L;
		Session session = Session.builder().id(id).name("Session").build();
		SessionDto sessionDto = new SessionDto();
		sessionDto.setId(id);
		sessionDto.setName(session.getName());

		when(sessionService.getById(id)).thenReturn(session);
		when(sessionMapper.toDto(session)).thenReturn(sessionDto);

		ResponseEntity<?> response = sessionController.findById("" + id);

		verify(sessionService).getById(id);
		verify(sessionMapper).toDto(session);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(response.getBody(), sessionDto);

	}

	@Test
	public void testFindSessionByIdNotFound() {

		Long id = 1L;

		when(sessionService.getById(id)).thenReturn(null);

		ResponseEntity<?> response = sessionController.findById("" + id);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());

	}

	@Test
	public void testFindSessionByIdBadRequest() {

		ResponseEntity<?> response = sessionController.findById("");

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNull(response.getBody());

	}

	@Test
	public void testFindAllSession() {

		Long id = 1L;
		Long id2 = 2L;

		List<Session> sessions = new ArrayList<>();
		sessions.add(Session.builder().id(id).build());
		sessions.add(Session.builder().id(id2).build());

		SessionDto sessionDto = new SessionDto();
		sessionDto.setId(id);

		SessionDto sessionDto2 = new SessionDto();
		sessionDto.setId(id2);

		List<SessionDto> sessionsDto = List.of(sessionDto, sessionDto2);

		when(sessionService.findAll()).thenReturn(sessions);
		when(sessionMapper.toDto(sessions)).thenReturn(sessionsDto);

		ResponseEntity<?> response = sessionController.findAll();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(sessionsDto, response.getBody());

	}

	@Test
	public void testCreateSession() {
		String name = "New Session";

		Session session = Session.builder().name(name).build();

		SessionDto sessionDto = new SessionDto();
		sessionDto.setName(name);

		when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
		when(sessionService.create(session)).thenReturn(session);
		when(sessionMapper.toDto(session)).thenReturn(sessionDto);

		ResponseEntity<?> response = sessionController.create(sessionDto);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(sessionDto, response.getBody());
	}

	@Test
	public void testUpdateSession() {

		Long id = 1L;
		String name = "Session";

		Session session = Session.builder().id(id).name(name).build();

		SessionDto sessionDto = new SessionDto();
		sessionDto.setId(id);
		sessionDto.setName(name);

		when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
		when(sessionService.update(id, session)).thenReturn(session);
		when(sessionMapper.toDto(session)).thenReturn(sessionDto);

		ResponseEntity<?> response = sessionController.update("" + id, sessionDto);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(sessionDto, response.getBody());
	}

	@Test
	public void testUpdateSessionBadRequest() {

		ResponseEntity<?> response = sessionController.update("", null);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	public void testDeleteSession() {
		Long id = 1L;

		when(sessionService.getById(id)).thenReturn(new Session());
		doNothing().when(sessionService).delete(id);

		ResponseEntity<?> response = sessionController.save("" + id);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testDeleteSessionNotFound() {
		Long id = 1L;
		when(sessionService.getById(id)).thenReturn(null);

		ResponseEntity<?> response = sessionController.save("" + id);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	public void testDeleteSessionBadRequest() {

		ResponseEntity<?> response = sessionController.save("");

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	public void testParticipateSession() {

		Long id = 1L;
		Long userId = 2L;

		doNothing().when(sessionService).participate(id, userId);

		ResponseEntity<?> response = sessionController.participate("" + id, "" + userId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testParticipateSessionBadRequest() {

		ResponseEntity<?> response = sessionController.participate("", "");
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNull(response.getBody());
		assertNull(response.getBody());
	}

	@Test
	public void testNoLongerParticipateSession() {
		Long id = 1L;
		Long userId = 2L;

		doNothing().when(sessionService).noLongerParticipate(id, userId);

		ResponseEntity<?> response = sessionController.noLongerParticipate("" + id, "" + userId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testNoLongerParticipateSessionBadRequest() {

		ResponseEntity<?> response = sessionController.noLongerParticipate("", "");

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNull(response.getBody());
	}

}
