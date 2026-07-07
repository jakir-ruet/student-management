package com.jakirbd.student_management.acss.service;

import com.jakirbd.student_management.acss.dto.request.AcademicYearCreateRequest;
import com.jakirbd.student_management.acss.dto.response.AcademicYearResponse;
import com.jakirbd.student_management.acss.dto.request.AcademicYearUpdateRequest;

import java.util.List;

public interface AcademicYearService {

    AcademicYearResponse createAcademicYear(
            AcademicYearCreateRequest request
    );

    AcademicYearResponse updateAcademicYear(
            Long academicYearId,
            AcademicYearUpdateRequest request
    );

    AcademicYearResponse getAcademicYearById(
            Long academicYearId
    );

    List<AcademicYearResponse> getAllAcademicYears();

    void deleteAcademicYear(
            Long academicYearId
    );
}
