package com.jakirbd.student_management.fee.repository;

import com.jakirbd.student_management.fee.dto.response.StudentFeeDiscountResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface StudentFeeDiscountRepository {

    Long createDiscount(
            Long studentFeeId,
            String discountType,
            BigDecimal discountValue,
            String reason,
            Long createdBy
    );

    Optional<StudentFeeDiscountResponse> findDiscountById(
            Long studentFeeDiscountId
    );

    List<StudentFeeDiscountResponse> findAllDiscounts();

    List<StudentFeeDiscountResponse> findDiscountsByStudentFee(
            Long studentFeeId
    );

    List<StudentFeeDiscountResponse> findDiscountsByStatus(
            String approvalStatus
    );

    void approveDiscount(
            Long studentFeeDiscountId,
            Long approvedBy
    );

    void rejectDiscount(
            Long studentFeeDiscountId,
            Long updatedBy
    );

    void cancelDiscount(
            Long studentFeeDiscountId,
            Long updatedBy
    );

    void deleteDiscount(Long studentFeeDiscountId);
}