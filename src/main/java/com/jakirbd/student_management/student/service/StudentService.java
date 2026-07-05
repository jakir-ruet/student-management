package com.jakirbd.student_management.student.service;

import com.jakirbd.student_management.student.dto.StudentCreateRequest;
import com.jakirbd.student_management.student.dto.StudentResponse;
import com.jakirbd.student_management.student.dto.StudentUpdateRequest;

import java.util.List;

public interface StudentService {

    Long addStudent(StudentCreateRequest request);

    void updateStudent(Long studentId, StudentUpdateRequest request);

    void deleteStudent(Long studentId);

    StudentResponse getStudentById(Long studentId);

    List<StudentResponse> getAllStudents();

    List<StudentResponse> searchStudents(String searchText);
}
