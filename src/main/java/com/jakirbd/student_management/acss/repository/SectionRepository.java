package com.jakirbd.student_management.acss.repository;

import com.jakirbd.student_management.acss.dto.SectionResponse;
import com.jakirbd.student_management.acss.model.Section;

import java.util.List;
import java.util.Optional;

public interface SectionRepository {

    Long createSection(
            Long academicYearClassId,
            String sectionName,
            String sectionCode,
            Integer capacity,
            Integer displayOrder,
            String status
    );

    void updateSection(
            Long sectionId,
            Long academicYearClassId,
            String sectionName,
            String sectionCode,
            Integer capacity,
            Integer displayOrder,
            String status
    );

    Optional<SectionResponse> findSectionById(Long sectionId);

    List<Section> findSectionsByAcademicYearClass(
            Long academicYearClassId
    );

    List<SectionResponse> findAllSections();

    void deleteSection(Long sectionId);
}
