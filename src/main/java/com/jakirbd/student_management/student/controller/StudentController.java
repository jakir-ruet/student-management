package com.jakirbd.student_management.student.controller;

import com.jakirbd.student_management.student.dto.StudentCreateRequest;
import com.jakirbd.student_management.student.dto.StudentResponse;
import com.jakirbd.student_management.student.dto.StudentUpdateRequest;
import com.jakirbd.student_management.student.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Long> addStudent(@RequestBody StudentCreateRequest request) {
        Long studentId = studentService.addStudent(request);
        return ResponseEntity.ok(studentId);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<String> updateStudent(
            @PathVariable Long studentId,
            @RequestBody StudentUpdateRequest request
    ) {
        studentService.updateStudent(studentId, request);
        return ResponseEntity.ok("Student updated successfully");
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.ok("Student deleted successfully");
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.getStudentById(studentId));
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentResponse>> searchStudents(
            @RequestParam String q
    ) {
        return ResponseEntity.ok(studentService.searchStudents(q));
    }
}
