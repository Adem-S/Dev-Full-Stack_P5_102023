package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {
	@Mock
	private TeacherMapper teacherMapper;
	@Mock
	private TeacherService teacherService;

	private TeacherController teacherController;

	@BeforeEach
	void setUp() {
		teacherController = new TeacherController(teacherService, teacherMapper);
	}

	@Test
	public void testFindTeacherById() {
		Long id = 1L;
		String firstname = "firstname";
		String lastname = "lastname";
		Teacher teacher = Teacher.builder().id(id).firstName(firstname).lastName(lastname).build();
		TeacherDto teacherDto = new TeacherDto();
		teacherDto.setId(id);
		teacherDto.setFirstName(firstname);
		teacherDto.setLastName(lastname);

		when(teacherService.findById(id)).thenReturn(teacher);
		when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

		ResponseEntity<?> response = teacherController.findById("" + id);
		TeacherDto responseBody = (TeacherDto) response.getBody();

		assertEquals(HttpStatus.OK, response.getStatusCode());

		assertEquals(teacherDto, responseBody);

	}

	@Test
	public void testFindTeacherByIdNotFound() {
		Long id = 1L;

		when(teacherService.findById(id)).thenReturn(null);

		ResponseEntity<?> response = teacherController.findById("" + id);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());

	}

	@Test
	public void testFindTeacherByIdBadRequest() {
		ResponseEntity<?> response = teacherController.findById("");

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	public void testFindAllTeacher() {

		Long id = 1L;
		Long id2 = 2L;

		List<Teacher> teachers = new ArrayList<>();
		teachers.add(Teacher.builder().id(id).build());
		teachers.add(Teacher.builder().id(id2).build());

		TeacherDto teacherDto = new TeacherDto();
		teacherDto.setId(id);

		TeacherDto teacherDto2 = new TeacherDto();
		teacherDto2.setId(id2);

		List<TeacherDto> teachersDto = List.of(teacherDto, teacherDto2);

		when(teacherService.findAll()).thenReturn(teachers);
		when(teacherMapper.toDto(teachers)).thenReturn(teachersDto);

		ResponseEntity<?> response = teacherController.findAll();

		assertEquals(teachersDto, response.getBody());

	}

}
