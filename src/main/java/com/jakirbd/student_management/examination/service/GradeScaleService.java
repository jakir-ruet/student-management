package com.jakirbd.student_management.examination.service;

import com.jakirbd.student_management.examination.dto.request.GradeScaleCreateRequest;
import com.jakirbd.student_management.examination.dto.request.GradeScaleStatusUpdateRequest;
import com.jakirbd.student_management.examination.dto.request.GradeScaleUpdateRequest;
import com.jakirbd.student_management.examination.dto.response.GradeCalculationResponse;
import com.jakirbd.student_management.examination.dto.response.GradeScaleResponse;

import java.math.BigDecimal;
import java.util.List;

public interface GradeScaleService {

    Long createGradeScale(
            GradeScaleCreateRequest request
    );

    void updateGradeScale(
            Long gradeScaleId,
            GradeScaleUpdateRequest request
    );

    GradeScaleResponse getGradeScaleById(
            Long gradeScaleId
    );

    List<GradeScaleResponse> getAllGradeScales();

    List<GradeScaleResponse> getActiveGradeScales();

    GradeCalculationResponse calculateGrade(
            BigDecimal percentage
    );

    void updateGradeScaleStatus(
            Long gradeScaleId,
            GradeScaleStatusUpdateRequest request
    );

    void deleteGradeScale(Long gradeScaleId);
}