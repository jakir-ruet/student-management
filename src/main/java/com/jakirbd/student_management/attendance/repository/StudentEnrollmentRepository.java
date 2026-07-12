package com.jakirbd.student_management.attendance.repository;

import com.jakirbd.student_management.attendance.model.StudentEnrollment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentEnrollmentRepository {
    Long createEnrollment(
        Long studentId,
        Long academicYearClassId,
        Long sectionShiftId,
        String rollNumber,
        LocalDate enrollmentDate,
        String status,
        String remarks
    );

    void updateEnrollment(
        Long enrollmentId,
        Long academicYearClassId,
        Long sectionShiftId,
        String rollNumber,
        LocalDate enrollmentDate,
        String status,
        String remarks
    );

    Optional<StudentEnrollment> findEnrollmentById(
            Long enrollmentId
    );

    List<StudentEnrollment> findAllEnrollments();

    List<StudentEnrollment> findEnrollmentsByStudentId(
            Long studentId
    );

    List<StudentEnrollment> findEnrollmentsBySectionShiftId(
            Long sectionShiftId,
            String status
    );

    void deleteEnrollmentById(Long enrollmentId);
}
