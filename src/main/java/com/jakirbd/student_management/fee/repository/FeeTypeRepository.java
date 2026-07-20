package com.jakirbd.student_management.fee.repository;

import com.jakirbd.student_management.fee.dto.response.FeeTypeResponse;

import java.util.List;
import java.util.Optional;

public interface FeeTypeRepository {

    Long createFeeType(
            String feeTypeName,
            String feeTypeCode,
            String description,
            Integer displayOrder,
            String status,
            Long createdBy
    );

    void updateFeeType(
            Long feeTypeId,
            String feeTypeName,
            String feeTypeCode,
            String description,
            Integer displayOrder,
            String status,
            Long updatedBy
    );

    Optional<FeeTypeResponse> findFeeTypeById(
            Long feeTypeId
    );

    List<FeeTypeResponse> findAllFeeTypes();

    List<FeeTypeResponse> findActiveFeeTypes();

    void updateFeeTypeStatus(
            Long feeTypeId,
            String status,
            Long updatedBy
    );

    void deleteFeeType(Long feeTypeId);
}