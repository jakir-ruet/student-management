package com.jakirbd.student_management.attendance.controller;

import com.jakirbd.student_management.attendance.dto.request.StudentEnrollmentCreateRequest;
import com.jakirbd.student_management.attendance.dto.request.StudentEnrollmentUpdateRequest;
import com.jakirbd.student_management.attendance.dto.response.StudentEnrollmentResponse;
import com.jakirbd.student_management.attendance.service.StudentEnrollmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-enrollments")
public class StudentEnrollmentController {

    private final StudentEnrollmentService enrollmentService;

    public StudentEnrollmentController(
            StudentEnrollmentService enrollmentService
    ) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    public ResponseEntity<StudentEnrollmentResponse> createEnrollment(
            @Valid @RequestBody StudentEnrollmentCreateRequest request
    ) {
        StudentEnrollmentResponse response =
                enrollmentService.createEnrollment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{enrollmentId}")
    public ResponseEntity<StudentEnrollmentResponse> updateEnrollment(
            @PathVariable Long enrollmentId,
            @Valid @RequestBody StudentEnrollmentUpdateRequest request
    ) {
        return ResponseEntity.ok(
                enrollmentService.updateEnrollment(
                        enrollmentId,
                        request
                )
        );
    }

    @GetMapping("/{enrollmentId}")
    public ResponseEntity<StudentEnrollmentResponse> getEnrollmentById(
            @PathVariable Long enrollmentId
    ) {
        return ResponseEntity.ok(
                enrollmentService.getEnrollmentById(enrollmentId)
        );
    }

    @GetMapping
    public ResponseEntity<List<StudentEnrollmentResponse>>
    getAllEnrollments() {
        return ResponseEntity.ok(
                enrollmentService.getAllEnrollments()
        );
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<StudentEnrollmentResponse>>
    getEnrollmentsByStudentId(
            @PathVariable Long studentId
    ) {
        return ResponseEntity.ok(
                enrollmentService.getEnrollmentsByStudentId(
                        studentId
                )
        );
    }

    @GetMapping("/section-shift/{sectionShiftId}")
    public ResponseEntity<List<StudentEnrollmentResponse>>
    getEnrollmentsBySectionShiftId(
            @PathVariable Long sectionShiftId,
            @RequestParam(required = false) String status
    ) {
        return ResponseEntity.ok(
                enrollmentService.getEnrollmentsBySectionShiftId(
                        sectionShiftId,
                        status
                )
        );
    }

    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<Void> deleteEnrollmentById(
            @PathVariable Long enrollmentId
    ) {
        enrollmentService.deleteEnrollmentById(enrollmentId);
        return ResponseEntity.noContent().build();
    }
}