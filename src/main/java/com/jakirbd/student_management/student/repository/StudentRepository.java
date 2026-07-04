package com.jakirbd.student_management.student.repository;

import java.util.List;
import java.util.Optional;

import com.jakirbd.student_management.student.model.Student;

public interface StudentRepository {
	Long addStudent(Student student);

	void updateStudent(Student student);

	void deleteStudent(Long studentId);

	Optional<Student> getStudentById(Long studentId);

	List<Student> getAllStudents();

	List<Student> searchStudents(String searchText);

}
