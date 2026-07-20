package com.jakirbd.student_management.fee.repository;

import com.jakirbd.student_management.fee.dto.response.FeePaymentAllocationResponse;
import com.jakirbd.student_management.fee.dto.response.FeePaymentResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FeePaymentRepository {

    Long createPayment(
            Long studentId,
            String receiptNumber,
            LocalDateTime paymentDate,
            BigDecimal paymentAmount,
            String paymentMethod,
            String transactionReference,
            String notes,
            Long receivedBy,
            Long createdBy
    );

    Long allocatePayment(
            Long feePaymentId,
            Long studentFeeId,
            BigDecimal allocatedAmount,
            Long createdBy
    );

    void completePayment(
            Long feePaymentId,
            Long updatedBy
    );

    void cancelPayment(
            Long feePaymentId,
            Long updatedBy
    );

    void refundPayment(
            Long feePaymentId,
            Long updatedBy
    );

    Optional<FeePaymentResponse> findPaymentById(
            Long feePaymentId
    );

    List<FeePaymentResponse> findAllPayments();

    List<FeePaymentResponse> findPaymentsByStudent(
            Long studentId
    );

    List<FeePaymentResponse> findPaymentsByStatus(
            String paymentStatus
    );

    List<FeePaymentAllocationResponse>
    findAllocationsByPayment(
            Long feePaymentId
    );

    void deletePaymentAllocation(
            Long paymentAllocationId
    );
}