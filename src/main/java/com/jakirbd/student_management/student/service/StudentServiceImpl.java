package com.jakirbd.student_management.student.service;

import com.jakirbd.student_management.student.dto.StudentCreateRequest;
import com.jakirbd.student_management.student.dto.StudentResponse;
import com.jakirbd.student_management.student.dto.StudentUpdateRequest;
import com.jakirbd.student_management.student.model.Student;
import com.jakirbd.student_management.student.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Long addStudent(StudentCreateRequest request) {
        Student student = new Student();

        student.setStudentCode(request.getStudentCode());
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        student.setGender(request.getGender());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setAdmissionDate(request.getAdmissionDate());

        return studentRepository.addStudent(student);
    }

    @Override
    public void updateStudent(Long studentId, StudentUpdateRequest request) {
        Student student = new Student();

        student.setStudentId(studentId);
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        student.setGender(request.getGender());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setAdmissionDate(request.getAdmissionDate());
        student.setStatus(request.getStatus());

        studentRepository.updateStudent(student);
    }

    @Override
    public void deleteStudent(Long studentId) {
        studentRepository.deleteStudent(studentId);
    }

    @Override
    public StudentResponse getStudentById(Long studentId) {
        Student student = studentRepository.getStudentById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return mapToResponse(student);
    }

    @Override
    public List<StudentResponse> getAllStudents() {
        return studentRepository.getAllStudents()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<StudentResponse> searchStudents(String searchText) {
        return studentRepository.searchStudents(searchText)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private StudentResponse mapToResponse(Student student) {
        StudentResponse response = new StudentResponse();

        response.setStudentId(student.getStudentId());
        response.setStudentCode(student.getStudentCode());
        response.setFirstName(student.getFirstName());
        response.setLastName(student.getLastName());
        response.setFullName(student.getFullName());
        response.setEmail(student.getEmail());
        response.setPhone(student.getPhone());
        response.setGender(student.getGender());
        response.setDateOfBirth(student.getDateOfBirth());
        response.setAdmissionDate(student.getAdmissionDate());
        response.setStatus(student.getStatus());
        response.setCreatedAt(student.getCreatedAt());
        response.setUpdatedAt(student.getUpdatedAt());

        return response;
    }
}
