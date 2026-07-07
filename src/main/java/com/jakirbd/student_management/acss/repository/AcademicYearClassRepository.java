package com.jakirbd.student_management.acss.repository;

import java.util.List;
import java.util.Optional;

import com.jakirbd.student_management.acss.dto.response.AcademicYearClassResponse;

public interface AcademicYearClassRepository {

    Long createAcademicYearClass(
            Long academicYearId,
            Long classId,
            String status
    );

    void updateAcademicYearClass(
            Long academicYearClassId,
            Long academicYearId,
            Long classId,
            String status
    );

    Optional<AcademicYearClassResponse> findAcademicYearClassById(
            Long academicYearClassId
    );

    List<AcademicYearClassResponse> findClassesByAcademicYear(
            Long academicYearId
    );

    List<AcademicYearClassResponse> findAllAcademicYearClasses();

    void deleteAcademicYearClass(Long academicYearClassId);
}
