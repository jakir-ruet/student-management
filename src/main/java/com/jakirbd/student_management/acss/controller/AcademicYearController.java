package com.jakirbd.student_management.acss.controller;

import com.jakirbd.student_management.acss.dto.request.AcademicYearCreateRequest;
import com.jakirbd.student_management.acss.dto.request.AcademicYearUpdateRequest;
import com.jakirbd.student_management.acss.dto.response.AcademicYearResponse;
import com.jakirbd.student_management.acss.service.AcademicYearService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/acss/academic-years")
public class AcademicYearController {

    private final AcademicYearService academicYearService;

    public AcademicYearController(AcademicYearService academicYearService) {
        this.academicYearService = academicYearService;
    }

    @PostMapping
    public ResponseEntity<AcademicYearResponse> createAcademicYear(
            @RequestBody AcademicYearCreateRequest request
    ) {
        AcademicYearResponse response =
                academicYearService.createAcademicYear(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{academicYearId}")
    public ResponseEntity<AcademicYearResponse> updateAcademicYear(
            @PathVariable Long academicYearId,
            @RequestBody AcademicYearUpdateRequest request
    ) {
        AcademicYearResponse response =
                academicYearService.updateAcademicYear(academicYearId, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{academicYearId}")
    public ResponseEntity<AcademicYearResponse> getAcademicYearById(
            @PathVariable Long academicYearId
    ) {
        return ResponseEntity.ok(
                academicYearService.getAcademicYearById(academicYearId)
        );
    }

    @GetMapping
    public ResponseEntity<?> getAllAcademicYears() {
        return ResponseEntity.ok(
                academicYearService.getAllAcademicYears()
        );
    }

    @DeleteMapping("/{academicYearId}")
    public ResponseEntity<Void> deleteAcademicYear(
            @PathVariable Long academicYearId
    ) {
        academicYearService.deleteAcademicYear(academicYearId);
        return ResponseEntity.noContent().build();
    }
}
