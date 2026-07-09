package com.jakirbd.student_management.acss.service.impl;

import com.jakirbd.student_management.acss.dto.request.AcademicYearCreateRequest;
import com.jakirbd.student_management.acss.dto.request.AcademicYearUpdateRequest;
import com.jakirbd.student_management.acss.dto.response.AcademicYearResponse;
import com.jakirbd.student_management.acss.model.AcademicYear;
import com.jakirbd.student_management.acss.repository.AcademicYearRepository;
import com.jakirbd.student_management.acss.service.AcademicYearService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcademicYearServiceImpl implements AcademicYearService {

    private final AcademicYearRepository academicYearRepository;

    public AcademicYearServiceImpl(AcademicYearRepository academicYearRepository) {
        this.academicYearRepository = academicYearRepository;
    }

    @Override
    public AcademicYearResponse createAcademicYear(AcademicYearCreateRequest request) {
        Long academicYearId = academicYearRepository.createAcademicYear(
                request.getYearName(),
                request.getStartDate(),
                request.getEndDate(),
                request.getIsCurrent(),
                request.getStatus()
        );

        return getAcademicYearById(academicYearId);
    }

    @Override
    public AcademicYearResponse updateAcademicYear(
            Long academicYearId,
            AcademicYearUpdateRequest request
    ) {
        academicYearRepository.updateAcademicYear(
                academicYearId,
                request.getYearName(),
                request.getStartDate(),
                request.getEndDate(),
                request.getIsCurrent(),
                request.getStatus()
        );

        return getAcademicYearById(academicYearId);
    }

    @Override
    public AcademicYearResponse getAcademicYearById(Long academicYearId) {
        AcademicYear academicYear = academicYearRepository.findAcademicYearById(academicYearId)
                .orElseThrow(() -> new RuntimeException("Academic year not found with ID: " + academicYearId));

        return mapToResponse(academicYear);
    }

    @Override
    public List<AcademicYearResponse> getAllAcademicYears() {
        return academicYearRepository.findAllAcademicYears()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteAcademicYear(Long academicYearId) {
        getAcademicYearById(academicYearId);
        academicYearRepository.deleteAcademicYear(academicYearId);
    }

    private AcademicYearResponse mapToResponse(AcademicYear academicYear) {
        AcademicYearResponse response = new AcademicYearResponse();

        response.setAcademicYearId(academicYear.getAcademicYearId());
        response.setYearName(academicYear.getYearName());
        response.setStartDate(academicYear.getStartDate());
        response.setEndDate(academicYear.getEndDate());
        response.setIsCurrent(academicYear.getIsCurrent());
        response.setStatus(academicYear.getStatus());
        response.setCreatedAt(academicYear.getCreatedAt());
        response.setUpdatedAt(academicYear.getUpdatedAt());

        return response;
    }
}
