package com.jakirbd.student_management.acss.service.impl;

import com.jakirbd.student_management.acss.dto.request.SectionCreateRequest;
import com.jakirbd.student_management.acss.dto.request.SectionUpdateRequest;
import com.jakirbd.student_management.acss.dto.response.SectionResponse;
import com.jakirbd.student_management.acss.model.Section;
import com.jakirbd.student_management.acss.repository.SectionRepository;
import com.jakirbd.student_management.acss.service.SectionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionServiceImpl implements SectionService {

    private final SectionRepository sectionRepository;

    public SectionServiceImpl(
            SectionRepository sectionRepository
    ) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public SectionResponse createSection(
            SectionCreateRequest request
    ) {
        Long sectionId = sectionRepository.createSection(
                request.getAcademicYearClassId(),
                request.getSectionName(),
                request.getSectionCode(),
                request.getCapacity(),
                request.getDisplayOrder(),
                request.getStatus()
        );

        return getSectionById(sectionId);
    }

    @Override
    public SectionResponse updateSection(
            Long sectionId,
            SectionUpdateRequest request
    ) {
        sectionRepository.updateSection(
                sectionId,
                request.getAcademicYearClassId(),
                request.getSectionName(),
                request.getSectionCode(),
                request.getCapacity(),
                request.getDisplayOrder(),
                request.getStatus()
        );

        return getSectionById(sectionId);
    }

    @Override
    public SectionResponse getSectionById(
            Long sectionId
    ) {
        return sectionRepository
                .findSectionById(sectionId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Section not found with ID: " + sectionId
                        )
                );
    }

    @Override
    public List<Section> getSectionsByAcademicYearClass(
            Long academicYearClassId
    ) {
        return sectionRepository
                .findSectionsByAcademicYearClass(
                        academicYearClassId
                );
    }

    @Override
    public List<SectionResponse> getAllSections() {
        return sectionRepository.findAllSections();
    }

    @Override
    public void deleteSection(
            Long sectionId
    ) {
        getSectionById(sectionId);

        sectionRepository.deleteSection(sectionId);
    }
}
