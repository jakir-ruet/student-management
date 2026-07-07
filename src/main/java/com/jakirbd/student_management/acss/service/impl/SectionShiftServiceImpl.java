package com.jakirbd.student_management.acss.service.impl;

import com.jakirbd.student_management.acss.dto.request.SectionShiftCreateRequest;
import com.jakirbd.student_management.acss.dto.request.SectionShiftUpdateRequest;
import com.jakirbd.student_management.acss.dto.response.SectionShiftResponse;
import com.jakirbd.student_management.acss.repository.SectionShiftRepository;
import com.jakirbd.student_management.acss.service.SectionShiftService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionShiftServiceImpl implements SectionShiftService {

    private final SectionShiftRepository sectionShiftRepository;

    public SectionShiftServiceImpl(SectionShiftRepository sectionShiftRepository) {
        this.sectionShiftRepository = sectionShiftRepository;
    }

    @Override
    public SectionShiftResponse createSectionShift(SectionShiftCreateRequest request) {
        Long sectionShiftId = sectionShiftRepository.createSectionShift(
                request.getSectionId(),
                request.getShiftId(),
                request.getStatus()
        );

        return getSectionShiftById(sectionShiftId);
    }

    @Override
    public SectionShiftResponse updateSectionShift(
            Long sectionShiftId,
            SectionShiftUpdateRequest request
    ) {
        sectionShiftRepository.updateSectionShift(
                sectionShiftId,
                request.getSectionId(),
                request.getShiftId(),
                request.getStatus()
        );

        return getSectionShiftById(sectionShiftId);
    }

    @Override
    public SectionShiftResponse getSectionShiftById(Long sectionShiftId) {
        return sectionShiftRepository.findSectionShiftById(sectionShiftId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Section shift not found with ID: " + sectionShiftId
                        )
                );
    }

    @Override
    public List<SectionShiftResponse> getShiftsBySection(Long sectionId) {
        return sectionShiftRepository.findShiftsBySection(sectionId);
    }

    @Override
    public List<SectionShiftResponse> getAllSectionShifts() {
        return sectionShiftRepository.findAllSectionShifts();
    }

    @Override
    public void deleteSectionShift(Long sectionShiftId) {
        getSectionShiftById(sectionShiftId);
        sectionShiftRepository.deleteSectionShift(sectionShiftId);
    }
}
