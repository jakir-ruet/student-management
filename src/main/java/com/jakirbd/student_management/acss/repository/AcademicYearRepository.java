package com.jakirbd.student_management.acss.repository;

import com.jakirbd.student_management.acss.model.AcademicYear;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AcademicYearRepository {

    Long createAcademicYear(
            String yearName,
            LocalDate startDate,
            LocalDate endDate,
            String isCurrent,
            String status
    );

    void updateAcademicYear(
            Long academicYearId,
            String yearName,
            LocalDate startDate,
            LocalDate endDate,
            String isCurrent,
            String status
    );

    Optional<AcademicYear> findAcademicYearById(Long academicYearId);

    List<AcademicYear> findAllAcademicYears();

    void deleteAcademicYear(Long academicYearId);
}
