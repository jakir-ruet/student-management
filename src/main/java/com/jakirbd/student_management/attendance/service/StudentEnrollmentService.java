package com.jakirbd.student_management.attendance.service;

import com.jakirbd.student_management.attendance.dto.request.StudentEnrollmentCreateRequest;
import com.jakirbd.student_management.attendance.dto.request.StudentEnrollmentUpdateRequest;
import com.jakirbd.student_management.attendance.dto.response.StudentEnrollmentResponse;

import java.util.List;

public interface StudentEnrollmentService {

    StudentEnrollmentResponse createEnrollment(
            StudentEnrollmentCreateRequest request
    );

    StudentEnrollmentResponse updateEnrollment(
            Long enrollmentId,
            StudentEnrollmentUpdateRequest request
    );

    StudentEnrollmentResponse getEnrollmentById(
            Long enrollmentId
    );

    List<StudentEnrollmentResponse> getAllEnrollments();

    List<StudentEnrollmentResponse> getEnrollmentsByStudentId(
            Long studentId
    );

    List<StudentEnrollmentResponse> getEnrollmentsBySectionShiftId(
            Long sectionShiftId,
            String status
    );

    void deleteEnrollmentById(Long enrollmentId);
}