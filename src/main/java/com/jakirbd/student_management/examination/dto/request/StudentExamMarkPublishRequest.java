package com.jakirbd.student_management.examination.dto.request;

import jakarta.validation.constraints.NotNull;

public class StudentExamMarkPublishRequest {

    @NotNull(message = "Updated by user ID is required")
    private Long updatedBy;

    public StudentExamMarkPublishRequest() {
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
}