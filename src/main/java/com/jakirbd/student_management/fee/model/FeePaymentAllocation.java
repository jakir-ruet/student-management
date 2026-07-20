package com.jakirbd.student_management.fee.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FeePaymentAllocation {

    private Long paymentAllocationId;
    private Long feePaymentId;
    private Long studentFeeId;
    private BigDecimal allocatedAmount;
    private LocalDateTime createdAt;
    private Long createdBy;

    public FeePaymentAllocation() {
    }

    public FeePaymentAllocation(
            Long paymentAllocationId,
            Long feePaymentId,
            Long studentFeeId,
            BigDecimal allocatedAmount,
            LocalDateTime createdAt,
            Long createdBy
    ) {
        this.paymentAllocationId = paymentAllocationId;
        this.feePaymentId = feePaymentId;
        this.studentFeeId = studentFeeId;
        this.allocatedAmount = allocatedAmount;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public Long getPaymentAllocationId() {
        return paymentAllocationId;
    }

    public void setPaymentAllocationId(
            Long paymentAllocationId
    ) {
        this.paymentAllocationId = paymentAllocationId;
    }

    public Long getFeePaymentId() {
        return feePaymentId;
    }

    public void setFeePaymentId(Long feePaymentId) {
        this.feePaymentId = feePaymentId;
    }

    public Long getStudentFeeId() {
        return studentFeeId;
    }

    public void setStudentFeeId(Long studentFeeId) {
        this.studentFeeId = studentFeeId;
    }

    public BigDecimal getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(
            BigDecimal allocatedAmount
    ) {
        this.allocatedAmount = allocatedAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}