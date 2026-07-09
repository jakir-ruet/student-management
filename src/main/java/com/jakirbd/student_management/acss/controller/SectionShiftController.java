package com.jakirbd.student_management.acss.controller;

import com.jakirbd.student_management.acss.dto.request.SectionShiftCreateRequest;
import com.jakirbd.student_management.acss.dto.request.SectionShiftUpdateRequest;
import com.jakirbd.student_management.acss.dto.response.SectionShiftResponse;
import com.jakirbd.student_management.acss.service.SectionShiftService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/acss/section-shifts")
public class SectionShiftController {

    private final SectionShiftService sectionShiftService;

    public SectionShiftController(SectionShiftService sectionShiftService) {
        this.sectionShiftService = sectionShiftService;
    }

    @PostMapping
    public ResponseEntity<SectionShiftResponse> createSectionShift(
            @RequestBody SectionShiftCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sectionShiftService.createSectionShift(request));
    }

    @PutMapping("/{sectionShiftId}")
    public ResponseEntity<SectionShiftResponse> updateSectionShift(
            @PathVariable Long sectionShiftId,
            @RequestBody SectionShiftUpdateRequest request
    ) {
        return ResponseEntity.ok(
                sectionShiftService.updateSectionShift(sectionShiftId, request)
        );
    }

    @GetMapping("/{sectionShiftId}")
    public ResponseEntity<SectionShiftResponse> getSectionShiftById(
            @PathVariable Long sectionShiftId
    ) {
        return ResponseEntity.ok(
                sectionShiftService.getSectionShiftById(sectionShiftId)
        );
    }

    @GetMapping("/by-section/{sectionId}")
    public ResponseEntity<List<SectionShiftResponse>> getShiftsBySection(
            @PathVariable Long sectionId
    ) {
        return ResponseEntity.ok(
                sectionShiftService.getShiftsBySection(sectionId)
        );
    }

    @GetMapping
    public ResponseEntity<List<SectionShiftResponse>> getAllSectionShifts() {
        return ResponseEntity.ok(
                sectionShiftService.getAllSectionShifts()
        );
    }

    @DeleteMapping("/{sectionShiftId}")
    public ResponseEntity<Void> deleteSectionShift(
            @PathVariable Long sectionShiftId
    ) {
        sectionShiftService.deleteSectionShift(sectionShiftId);
        return ResponseEntity.noContent().build();
    }
}
