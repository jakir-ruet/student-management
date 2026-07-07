package com.jakirbd.student_management.acss.service.impl;

import com.jakirbd.student_management.acss.dto.request.AcademicYearClassCreateRequest;
import com.jakirbd.student_management.acss.dto.request.AcademicYearClassUpdateRequest;
import com.jakirbd.student_management.acss.dto.response.AcademicYearClassResponse;
import com.jakirbd.student_management.acss.repository.AcademicYearClassRepository;
import com.jakirbd.student_management.acss.service.AcademicYearClassService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcademicYearClassServiceImpl implements AcademicYearClassService {

    private final AcademicYearClassRepository academicYearClassRepository;

    public AcademicYearClassServiceImpl(
            AcademicYearClassRepository academicYearClassRepository
    ) {
        this.academicYearClassRepository = academicYearClassRepository;
    }

    @Override
    public AcademicYearClassResponse createAcademicYearClass(
            AcademicYearClassCreateRequest request
    ) {
        Long academicYearClassId =
                academicYearClassRepository.createAcademicYearClass(
                        request.getAcademicYearId(),
                        request.getClassId(),
                        request.getStatus()
                );

        return getAcademicYearClassById(academicYearClassId);
    }

    @Override
    public AcademicYearClassResponse updateAcademicYearClass(
            Long academicYearClassId,
            AcademicYearClassUpdateRequest request
    ) {
        academicYearClassRepository.updateAcademicYearClass(
                academicYearClassId,
                request.getAcademicYearId(),
                request.getClassId(),
                request.getStatus()
        );

        return getAcademicYearClassById(academicYearClassId);
    }

    @Override
    public AcademicYearClassResponse getAcademicYearClassById(
            Long academicYearClassId
    ) {
        return academicYearClassRepository
                .findAcademicYearClassById(academicYearClassId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Academic year class not found with ID: "
                                        + academicYearClassId
                        )
                );
    }

    @Override
    public List<AcademicYearClassResponse> getClassesByAcademicYear(
            Long academicYearId
    ) {
        return academicYearClassRepository
                .findClassesByAcademicYear(academicYearId);
    }

    @Override
    public List<AcademicYearClassResponse> getAllAcademicYearClasses() {
        return academicYearClassRepository
                .findAllAcademicYearClasses();
    }

    @Override
    public void deleteAcademicYearClass(
            Long academicYearClassId
    ) {
        getAcademicYearClassById(academicYearClassId);

        academicYearClassRepository.deleteAcademicYearClass(
                academicYearClassId
        );
    }
}
