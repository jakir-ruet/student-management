package com.jakirbd.student_management.examination.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ExamStatusUpdateRequest {

    @NotBlank(message = "Status is required")
    @Pattern(
            regexp = "DRAFT|SCHEDULED|ONGOING|COMPLETED|CANCELLED",
            message = "Status must be DRAFT, SCHEDULED, ONGOING, " +
                    "COMPLETED, or CANCELLED"
    )
    private String status;

    private Long updatedBy;

    public ExamStatusUpdateRequest() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
}