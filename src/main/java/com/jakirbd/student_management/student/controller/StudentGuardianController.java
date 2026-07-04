package com.jakirbd.student_management.student.controller;

import com.jakirbd.student_management.student.dto.StudentGuardianCreateRequest;
import com.jakirbd.student_management.student.dto.StudentGuardianResponse;
import com.jakirbd.student_management.student.dto.StudentGuardianUpdateRequest;
import com.jakirbd.student_management.student.service.StudentGuardianService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-guardians")
public class StudentGuardianController {

    private final StudentGuardianService studentGuardianService;

    public StudentGuardianController(StudentGuardianService studentGuardianService) {
        this.studentGuardianService = studentGuardianService;
    }

    @PostMapping
    public ResponseEntity<Long> addGuardian(@RequestBody StudentGuardianCreateRequest request) {
        Long guardianId = studentGuardianService.addGuardian(request);
        return ResponseEntity.ok(guardianId);
    }

    @PutMapping("/{guardianId}")
    public ResponseEntity<String> updateGuardian(
            @PathVariable Long guardianId,
            @RequestBody StudentGuardianUpdateRequest request
    ) {
        studentGuardianService.updateGuardian(guardianId, request);
        return ResponseEntity.ok("Student guardian updated successfully");
    }

    @DeleteMapping("/{guardianId}")
    public ResponseEntity<String> deleteGuardian(@PathVariable Long guardianId) {
        studentGuardianService.deleteGuardian(guardianId);
        return ResponseEntity.ok("Student guardian deleted successfully");
    }

    @GetMapping("/{guardianId}")
    public ResponseEntity<StudentGuardianResponse> getGuardianById(@PathVariable Long guardianId) {
        return ResponseEntity.ok(studentGuardianService.getGuardianById(guardianId));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<StudentGuardianResponse>> getGuardiansByStudent(
            @PathVariable Long studentId
    ) {
        return ResponseEntity.ok(studentGuardianService.getGuardiansByStudent(studentId));
    }
}
