package com.jakirbd.student_management.examination.repository;

import com.jakirbd.student_management.examination.dto.response.GradeCalculationResponse;
import com.jakirbd.student_management.examination.dto.response.GradeScaleResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface GradeScaleRepository {

    Long createGradeScale(
            String gradeName,
            String letterGrade,
            BigDecimal minPercentage,
            BigDecimal maxPercentage,
            BigDecimal gradePoint,
            String description,
            Integer displayOrder,
            String status,
            Long createdBy
    );

    void updateGradeScale(
            Long gradeScaleId,
            String gradeName,
            String letterGrade,
            BigDecimal minPercentage,
            BigDecimal maxPercentage,
            BigDecimal gradePoint,
            String description,
            Integer displayOrder,
            String status,
            Long updatedBy
    );

    Optional<GradeScaleResponse> findGradeScaleById(
            Long gradeScaleId
    );

    List<GradeScaleResponse> findAllGradeScales();

    List<GradeScaleResponse> findActiveGradeScales();

    GradeCalculationResponse calculateGrade(
            BigDecimal percentage
    );

    void updateGradeScaleStatus(
            Long gradeScaleId,
            String status,
            Long updatedBy
    );

    void deleteGradeScale(Long gradeScaleId);
}