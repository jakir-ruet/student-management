package com.jakirbd.student_management.fee.dto.request;

import jakarta.validation.constraints.NotNull;

public class FeePaymentActionRequest {

    @NotNull(message = "Updated by user ID is required")
    private Long updatedBy;

    public FeePaymentActionRequest() {
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
}