package com.jakirbd.student_management.examination.controller;

import com.jakirbd.student_management.examination.dto.request.GradeScaleCreateRequest;
import com.jakirbd.student_management.examination.dto.request.GradeScaleStatusUpdateRequest;
import com.jakirbd.student_management.examination.dto.request.GradeScaleUpdateRequest;
import com.jakirbd.student_management.examination.dto.response.GradeCalculationResponse;
import com.jakirbd.student_management.examination.dto.response.GradeScaleResponse;
import com.jakirbd.student_management.examination.service.GradeScaleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/grade-scales")
@Validated
public class GradeScaleController {

    private final GradeScaleService gradeScaleService;

    public GradeScaleController(
            GradeScaleService gradeScaleService
    ) {
        this.gradeScaleService = gradeScaleService;
    }

    @PostMapping
    public ResponseEntity<GradeScaleResponse> createGradeScale(
            @Valid @RequestBody GradeScaleCreateRequest request
    ) {
        Long gradeScaleId =
                gradeScaleService.createGradeScale(request);

        GradeScaleResponse response =
                gradeScaleService.getGradeScaleById(
                        gradeScaleId
                );

        return ResponseEntity
                .created(
                        URI.create(
                                "/api/grade-scales/"
                                        + gradeScaleId
                        )
                )
                .body(response);
    }

    @PutMapping("/{gradeScaleId}")
    public ResponseEntity<GradeScaleResponse> updateGradeScale(
            @PathVariable Long gradeScaleId,
            @Valid @RequestBody GradeScaleUpdateRequest request
    ) {
        gradeScaleService.updateGradeScale(
                gradeScaleId,
                request
        );

        return ResponseEntity.ok(
                gradeScaleService.getGradeScaleById(
                        gradeScaleId
                )
        );
    }

    @GetMapping("/{gradeScaleId}")
    public ResponseEntity<GradeScaleResponse>
    getGradeScaleById(
            @PathVariable Long gradeScaleId
    ) {
        return ResponseEntity.ok(
                gradeScaleService.getGradeScaleById(
                        gradeScaleId
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<GradeScaleResponse>>
    getAllGradeScales() {
        return ResponseEntity.ok(
                gradeScaleService.getAllGradeScales()
        );
    }

    @GetMapping("/active")
    public ResponseEntity<List<GradeScaleResponse>>
    getActiveGradeScales() {
        return ResponseEntity.ok(
                gradeScaleService.getActiveGradeScales()
        );
    }

    @GetMapping("/calculate")
    public ResponseEntity<GradeCalculationResponse>
    calculateGrade(
            @RequestParam
            @DecimalMin(
                    value = "0.00",
                    message = "Percentage cannot be less than zero"
            )
            @DecimalMax(
                    value = "100.00",
                    message = "Percentage cannot exceed 100"
            )
            BigDecimal percentage
    ) {
        return ResponseEntity.ok(
                gradeScaleService.calculateGrade(percentage)
        );
    }

    @PatchMapping("/{gradeScaleId}/status")
    public ResponseEntity<GradeScaleResponse>
    updateGradeScaleStatus(
            @PathVariable Long gradeScaleId,
            @Valid
            @RequestBody GradeScaleStatusUpdateRequest request
    ) {
        gradeScaleService.updateGradeScaleStatus(
                gradeScaleId,
                request
        );

        return ResponseEntity.ok(
                gradeScaleService.getGradeScaleById(
                        gradeScaleId
                )
        );
    }

    @DeleteMapping("/{gradeScaleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGradeScale(
            @PathVariable Long gradeScaleId
    ) {
        gradeScaleService.deleteGradeScale(gradeScaleId);
    }
}