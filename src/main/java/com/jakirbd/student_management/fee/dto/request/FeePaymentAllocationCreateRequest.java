package com.jakirbd.student_management.fee.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class FeePaymentAllocationCreateRequest {

    @NotNull(message = "Student fee ID is required")
    private Long studentFeeId;

    @NotNull(message = "Allocated amount is required")
    @DecimalMin(
            value = "0.01",
            message = "Allocated amount must be greater than zero"
    )
    private BigDecimal allocatedAmount;

    public FeePaymentAllocationCreateRequest() {
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
}