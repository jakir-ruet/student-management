package com.jakirbd.student_management.attendance.service.impl;

import com.jakirbd.student_management.attendance.dto.request.StudentEnrollmentCreateRequest;
import com.jakirbd.student_management.attendance.dto.request.StudentEnrollmentUpdateRequest;
import com.jakirbd.student_management.attendance.dto.response.StudentEnrollmentResponse;
import com.jakirbd.student_management.attendance.model.StudentEnrollment;
import com.jakirbd.student_management.attendance.repository.StudentEnrollmentRepository;
import com.jakirbd.student_management.attendance.service.StudentEnrollmentService;
import com.jakirbd.student_management.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentEnrollmentServiceImpl
        implements StudentEnrollmentService {

    private final StudentEnrollmentRepository enrollmentRepository;

    public StudentEnrollmentServiceImpl(
            StudentEnrollmentRepository enrollmentRepository
    ) {
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    @Transactional
    public StudentEnrollmentResponse createEnrollment(
            StudentEnrollmentCreateRequest request
    ) {
        Long enrollmentId =
                enrollmentRepository.createEnrollment(
                        request.getStudentId(),
                        request.getAcademicYearClassId(),
                        request.getSectionShiftId(),
                        request.getRollNumber(),
                        request.getEnrollmentDate(),
                        request.getStatus(),
                        request.getRemarks()
                );

        return getEnrollmentById(enrollmentId);
    }

    @Override
    @Transactional
    public StudentEnrollmentResponse updateEnrollment(
            Long enrollmentId,
            StudentEnrollmentUpdateRequest request
    ) {
        getEnrollmentEntityById(enrollmentId);

        enrollmentRepository.updateEnrollment(
                enrollmentId,
                request.getAcademicYearClassId(),
                request.getSectionShiftId(),
                request.getRollNumber(),
                request.getEnrollmentDate(),
                request.getStatus(),
                request.getRemarks()
        );

        return getEnrollmentById(enrollmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentEnrollmentResponse getEnrollmentById(
            Long enrollmentId
    ) {
        return mapToResponse(
                getEnrollmentEntityById(enrollmentId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentEnrollmentResponse> getAllEnrollments() {
        return enrollmentRepository.findAllEnrollments()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentEnrollmentResponse> getEnrollmentsByStudentId(
            Long studentId
    ) {
        return enrollmentRepository
                .findEnrollmentsByStudentId(studentId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentEnrollmentResponse>
    getEnrollmentsBySectionShiftId(
            Long sectionShiftId,
            String status
    ) {
        return enrollmentRepository
                .findEnrollmentsBySectionShiftId(
                        sectionShiftId,
                        status
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteEnrollmentById(Long enrollmentId) {
        getEnrollmentEntityById(enrollmentId);
        enrollmentRepository.deleteEnrollmentById(enrollmentId);
    }

    private StudentEnrollment getEnrollmentEntityById(
            Long enrollmentId
    ) {
        return enrollmentRepository
                .findEnrollmentById(enrollmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student enrollment not found with ID: "
                                        + enrollmentId
                        )
                );
    }

    private StudentEnrollmentResponse mapToResponse(
            StudentEnrollment enrollment
    ) {
        StudentEnrollmentResponse response =
                new StudentEnrollmentResponse();

        response.setEnrollmentId(enrollment.getEnrollmentId());
        response.setStudentId(enrollment.getStudentId());
        response.setAcademicYearClassId(
                enrollment.getAcademicYearClassId()
        );
        response.setSectionShiftId(
                enrollment.getSectionShiftId()
        );
        response.setRollNumber(enrollment.getRollNumber());
        response.setEnrollmentDate(
                enrollment.getEnrollmentDate()
        );
        response.setStatus(enrollment.getStatus());
        response.setRemarks(enrollment.getRemarks());
        response.setCreatedAt(enrollment.getCreatedAt());
        response.setUpdatedAt(enrollment.getUpdatedAt());

        return response;
    }
}