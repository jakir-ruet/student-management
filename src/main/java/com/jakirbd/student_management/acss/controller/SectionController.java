package com.jakirbd.student_management.acss.controller;

import com.jakirbd.student_management.acss.dto.request.SectionCreateRequest;
import com.jakirbd.student_management.acss.dto.request.SectionUpdateRequest;
import com.jakirbd.student_management.acss.dto.response.SectionResponse;
import com.jakirbd.student_management.acss.model.Section;
import com.jakirbd.student_management.acss.service.SectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/acss/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<SectionResponse> createSection(
            @RequestBody SectionCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sectionService.createSection(request));
    }

    @PutMapping("/{sectionId}")
    public ResponseEntity<SectionResponse> updateSection(
            @PathVariable Long sectionId,
            @RequestBody SectionUpdateRequest request
    ) {
        return ResponseEntity.ok(
                sectionService.updateSection(sectionId, request)
        );
    }

    @GetMapping("/{sectionId}")
    public ResponseEntity<SectionResponse> getSectionById(
            @PathVariable Long sectionId
    ) {
        return ResponseEntity.ok(
                sectionService.getSectionById(sectionId)
        );
    }

    @GetMapping("/by-academic-year-class/{academicYearClassId}")
    public ResponseEntity<List<Section>> getSectionsByAcademicYearClass(
            @PathVariable Long academicYearClassId
    ) {
        return ResponseEntity.ok(
                sectionService.getSectionsByAcademicYearClass(academicYearClassId)
        );
    }

    @GetMapping
    public ResponseEntity<List<SectionResponse>> getAllSections() {
        return ResponseEntity.ok(
                sectionService.getAllSections()
        );
    }

    @DeleteMapping("/{sectionId}")
    public ResponseEntity<Void> deleteSection(
            @PathVariable Long sectionId
    ) {
        sectionService.deleteSection(sectionId);
        return ResponseEntity.noContent().build();
    }
}
