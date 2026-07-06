package com.jakirbd.student_management.acss.repository;

import com.jakirbd.student_management.acss.dto.SectionShiftResponse;

import java.util.List;
import java.util.Optional;

public interface SectionShiftRepository {

    Long createSectionShift(
            Long sectionId,
            Long shiftId,
            String status
    );

    void updateSectionShift(
            Long sectionShiftId,
            Long sectionId,
            Long shiftId,
            String status
    );

    Optional<SectionShiftResponse> findSectionShiftById(
            Long sectionShiftId
    );

    List<SectionShiftResponse> findShiftsBySection(
            Long sectionId
    );

    List<SectionShiftResponse> findAllSectionShifts();

    void deleteSectionShift(Long sectionShiftId);
}
