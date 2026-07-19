package com.jakirbd.student_management.examination.service.impl;

import com.jakirbd.student_management.common.exception.ResourceNotFoundException;
import com.jakirbd.student_management.examination.dto.request.GradeScaleCreateRequest;
import com.jakirbd.student_management.examination.dto.request.GradeScaleStatusUpdateRequest;
import com.jakirbd.student_management.examination.dto.request.GradeScaleUpdateRequest;
import com.jakirbd.student_management.examination.dto.response.GradeCalculationResponse;
import com.jakirbd.student_management.examination.dto.response.GradeScaleResponse;
import com.jakirbd.student_management.examination.repository.GradeScaleRepository;
import com.jakirbd.student_management.examination.service.GradeScaleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

@Service
public class GradeScaleServiceImpl
        implements GradeScaleService {

    private static final BigDecimal ONE_HUNDRED =
            new BigDecimal("100.00");

    private final GradeScaleRepository gradeScaleRepository;

    public GradeScaleServiceImpl(
            GradeScaleRepository gradeScaleRepository
    ) {
        this.gradeScaleRepository = gradeScaleRepository;
    }

    @Override
    @Transactional
    public Long createGradeScale(
            GradeScaleCreateRequest request
    ) {
        validateGradeRange(
                request.getMinPercentage(),
                request.getMaxPercentage(),
                request.getGradePoint()
        );

        String status = request.getStatus() == null
                ? "ACTIVE"
                : normalize(request.getStatus());

        Integer displayOrder = request.getDisplayOrder() == null
                ? 1
                : request.getDisplayOrder();

        return gradeScaleRepository.createGradeScale(
                request.getGradeName().trim(),
                normalize(request.getLetterGrade()),
                request.getMinPercentage(),
                request.getMaxPercentage(),
                request.getGradePoint(),
                trimToNull(request.getDescription()),
                displayOrder,
                status,
                request.getCreatedBy()
        );
    }

    @Override
    @Transactional
    public void updateGradeScale(
            Long gradeScaleId,
            GradeScaleUpdateRequest request
    ) {
        validateGradeScaleId(gradeScaleId);

        validateGradeRange(
                request.getMinPercentage(),
                request.getMaxPercentage(),
                request.getGradePoint()
        );

        gradeScaleRepository.updateGradeScale(
                gradeScaleId,
                request.getGradeName().trim(),
                normalize(request.getLetterGrade()),
                request.getMinPercentage(),
                request.getMaxPercentage(),
                request.getGradePoint(),
                trimToNull(request.getDescription()),
                request.getDisplayOrder(),
                normalize(request.getStatus()),
                request.getUpdatedBy()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public GradeScaleResponse getGradeScaleById(
            Long gradeScaleId
    ) {
        validateGradeScaleId(gradeScaleId);

        return gradeScaleRepository
                .findGradeScaleById(gradeScaleId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Grade scale not found with ID: "
                                + gradeScaleId
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GradeScaleResponse> getAllGradeScales() {
        return gradeScaleRepository.findAllGradeScales();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GradeScaleResponse> getActiveGradeScales() {
        return gradeScaleRepository.findActiveGradeScales();
    }

    @Override
    @Transactional(readOnly = true)
    public GradeCalculationResponse calculateGrade(
            BigDecimal percentage
    ) {
        validatePercentage(percentage);
        return gradeScaleRepository.calculateGrade(percentage);
    }

    @Override
    @Transactional
    public void updateGradeScaleStatus(
            Long gradeScaleId,
            GradeScaleStatusUpdateRequest request
    ) {
        validateGradeScaleId(gradeScaleId);

        gradeScaleRepository.updateGradeScaleStatus(
                gradeScaleId,
                normalize(request.getStatus()),
                request.getUpdatedBy()
        );
    }

    @Override
    @Transactional
    public void deleteGradeScale(Long gradeScaleId) {
        validateGradeScaleId(gradeScaleId);
        gradeScaleRepository.deleteGradeScale(gradeScaleId);
    }

    private void validateGradeScaleId(Long gradeScaleId) {
        if (gradeScaleId == null || gradeScaleId <= 0) {
            throw new IllegalArgumentException(
                    "Grade scale ID must be greater than zero"
            );
        }
    }

    private void validateGradeRange(
            BigDecimal minPercentage,
            BigDecimal maxPercentage,
            BigDecimal gradePoint
    ) {
        validatePercentage(minPercentage);
        validatePercentage(maxPercentage);

        if (maxPercentage.compareTo(minPercentage) < 0) {
            throw new IllegalArgumentException(
                    "Maximum percentage cannot be less than " +
                            "minimum percentage"
            );
        }

        if (gradePoint == null
                || gradePoint.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Grade point cannot be negative"
            );
        }
    }

    private void validatePercentage(BigDecimal percentage) {
        if (percentage == null) {
            throw new IllegalArgumentException(
                    "Percentage is required"
            );
        }

        if (percentage.compareTo(BigDecimal.ZERO) < 0
                || percentage.compareTo(ONE_HUNDRED) > 0) {
            throw new IllegalArgumentException(
                    "Percentage must be between 0 and 100"
            );
        }
    }

    private String normalize(String value) {
        return value == null
                ? null
                : value.trim().toUpperCase(Locale.ROOT);
    }

    private String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}