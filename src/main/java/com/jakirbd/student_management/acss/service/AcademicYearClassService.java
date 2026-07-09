package com.jakirbd.student_management.acss.service;

import com.jakirbd.student_management.acss.dto.request.AcademicYearClassCreateRequest;
import com.jakirbd.student_management.acss.dto.request.AcademicYearClassUpdateRequest;
import com.jakirbd.student_management.acss.dto.response.AcademicYearClassResponse;

import java.util.List;

public interface AcademicYearClassService {

    AcademicYearClassResponse createAcademicYearClass(
            AcademicYearClassCreateRequest request
    );

    AcademicYearClassResponse updateAcademicYearClass(
            Long academicYearClassId,
            AcademicYearClassUpdateRequest request
    );

    AcademicYearClassResponse getAcademicYearClassById(
            Long academicYearClassId
    );

    List<AcademicYearClassResponse> getClassesByAcademicYear(
            Long academicYearId
    );

    List<AcademicYearClassResponse> getAllAcademicYearClasses();

    void deleteAcademicYearClass(
            Long academicYearClassId
    );
}
