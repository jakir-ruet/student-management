package com.jakirbd.student_management.acss.service;

import com.jakirbd.student_management.acss.dto.request.SectionShiftCreateRequest;
import com.jakirbd.student_management.acss.dto.request.SectionShiftUpdateRequest;
import com.jakirbd.student_management.acss.dto.response.SectionShiftResponse;

import java.util.List;

public interface SectionShiftService {

    SectionShiftResponse createSectionShift(
            SectionShiftCreateRequest request
    );

    SectionShiftResponse updateSectionShift(
            Long sectionShiftId,
            SectionShiftUpdateRequest request
    );

    SectionShiftResponse getSectionShiftById(
            Long sectionShiftId
    );

    List<SectionShiftResponse> getShiftsBySection(
            Long sectionId
    );

    List<SectionShiftResponse> getAllSectionShifts();

    void deleteSectionShift(
            Long sectionShiftId
    );
}
