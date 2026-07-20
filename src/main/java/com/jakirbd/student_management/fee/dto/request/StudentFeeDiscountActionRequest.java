package com.jakirbd.student_management.fee.dto.request;

import jakarta.validation.constraints.NotNull;

public class StudentFeeDiscountActionRequest {

    @NotNull(message = "Action by user ID is required")
    private Long actionBy;

    public StudentFeeDiscountActionRequest() {
    }

    public Long getActionBy() {
        return actionBy;
    }

    public void setActionBy(Long actionBy) {
        this.actionBy = actionBy;
    }
}