package com.jakirbd.student_management.acss.controller;

import com.jakirbd.student_management.acss.dto.request.AcademicYearClassCreateRequest;
import com.jakirbd.student_management.acss.dto.request.AcademicYearClassUpdateRequest;
import com.jakirbd.student_management.acss.dto.response.AcademicYearClassResponse;
import com.jakirbd.student_management.acss.service.AcademicYearClassService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/acss/academic-year-classes")
public class AcademicYearClassController {

    private final AcademicYearClassService academicYearClassService;

    public AcademicYearClassController(
            AcademicYearClassService academicYearClassService
    ) {
        this.academicYearClassService = academicYearClassService;
    }

    @PostMapping
    public ResponseEntity<AcademicYearClassResponse> createAcademicYearClass(
            @RequestBody AcademicYearClassCreateRequest request
    ) {
        AcademicYearClassResponse response =
                academicYearClassService.createAcademicYearClass(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{academicYearClassId}")
    public ResponseEntity<AcademicYearClassResponse> updateAcademicYearClass(
            @PathVariable Long academicYearClassId,
            @RequestBody AcademicYearClassUpdateRequest request
    ) {
        AcademicYearClassResponse response =
                academicYearClassService.updateAcademicYearClass(
                        academicYearClassId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{academicYearClassId}")
    public ResponseEntity<AcademicYearClassResponse> getAcademicYearClassById(
            @PathVariable Long academicYearClassId
    ) {
        AcademicYearClassResponse response =
                academicYearClassService.getAcademicYearClassById(
                        academicYearClassId
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-academic-year/{academicYearId}")
    public ResponseEntity<List<AcademicYearClassResponse>> getClassesByAcademicYear(
            @PathVariable Long academicYearId
    ) {
        List<AcademicYearClassResponse> responses =
                academicYearClassService.getClassesByAcademicYear(
                        academicYearId
                );

        return ResponseEntity.ok(responses);
    }

    @GetMapping
    public ResponseEntity<List<AcademicYearClassResponse>> getAllAcademicYearClasses() {
        List<AcademicYearClassResponse> responses =
                academicYearClassService.getAllAcademicYearClasses();

        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{academicYearClassId}")
    public ResponseEntity<Void> deleteAcademicYearClass(
            @PathVariable Long academicYearClassId
    ) {
        academicYearClassService.deleteAcademicYearClass(
                academicYearClassId
        );

        return ResponseEntity.noContent().build();
    }
}
