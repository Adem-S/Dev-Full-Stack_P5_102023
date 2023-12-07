package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

	@Mock
	private TeacherRepository teacherRepository;

	@InjectMocks
	private TeacherService teacherService;

	private Teacher teacher;

	@BeforeEach
	void setUp() {
		teacher = new Teacher(1L, "lastname", "firstname", LocalDateTime.now(), LocalDateTime.now());
	}

	@Test
	void testFindAllTeacher() {

		List<Teacher> teacherList = new ArrayList<>();
		teacherList.add(teacher);
		teacherList.add(teacher);

		when(teacherRepository.findAll()).thenReturn(teacherList);

		List<Teacher> result = teacherService.findAll();

		assertEquals(2, result.size());
		assertEquals(teacherList, result);
	}

	@Test
	void testFindTeacherById() {

		Long teacherId = 1L;

		when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

		Teacher result = teacherService.findById(teacherId);

		assertEquals(teacherId, result.getId());

	}
}
