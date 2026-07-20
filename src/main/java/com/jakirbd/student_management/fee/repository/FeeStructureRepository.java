package com.jakirbd.student_management.fee.repository;

import com.jakirbd.student_management.fee.dto.response.FeeStructureResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FeeStructureRepository {

    Long createFeeStructure(
            Long academicYearId,
            Long academicYearClassId,
            Long sectionId,
            Long feeTypeId,
            String structureName,
            BigDecimal amount,
            String frequency,
            Integer dueDay,
            LocalDate effectiveFrom,
            LocalDate effectiveTo,
            String fineType,
            BigDecimal fineValue,
            String description,
            String status,
            Long createdBy
    );

    void updateFeeStructure(
            Long feeStructureId,
            Long academicYearId,
            Long academicYearClassId,
            Long sectionId,
            Long feeTypeId,
            String structureName,
            BigDecimal amount,
            String frequency,
            Integer dueDay,
            LocalDate effectiveFrom,
            LocalDate effectiveTo,
            String fineType,
            BigDecimal fineValue,
            String description,
            String status,
            Long updatedBy
    );

    Optional<FeeStructureResponse> findFeeStructureById(
            Long feeStructureId
    );

    List<FeeStructureResponse> findAllFeeStructures();

    List<FeeStructureResponse> findFeeStructuresByYear(
            Long academicYearId
    );

    List<FeeStructureResponse> findFeeStructuresByClass(
            Long academicYearClassId
    );

    List<FeeStructureResponse> findFeeStructuresBySection(
            Long sectionId
    );

    List<FeeStructureResponse> findActiveFeeStructures();

    void updateFeeStructureStatus(
            Long feeStructureId,
            String status,
            Long updatedBy
    );

    void deleteFeeStructure(Long feeStructureId);
}