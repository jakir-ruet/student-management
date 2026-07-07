package com.jakirbd.student_management.acss.service;

import com.jakirbd.student_management.acss.dto.request.SectionCreateRequest;
import com.jakirbd.student_management.acss.dto.request.SectionUpdateRequest;
import com.jakirbd.student_management.acss.dto.response.SectionResponse;
import com.jakirbd.student_management.acss.model.Section;

import java.util.List;

public interface SectionService {

    SectionResponse createSection(
            SectionCreateRequest request
    );

    SectionResponse updateSection(
            Long sectionId,
            SectionUpdateRequest request
    );

    SectionResponse getSectionById(
            Long sectionId
    );

    List<Section> getSectionsByAcademicYearClass(
            Long academicYearClassId
    );

    List<SectionResponse> getAllSections();

    void deleteSection(
            Long sectionId
    );
}
