package com.jakirbd.student_management.examination.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ExamSubjectStatusUpdateRequest {

    @NotBlank(message = "Status is required")
    @Pattern(
            regexp = "SCHEDULED|ONGOING|COMPLETED|POSTPONED|CANCELLED",
            message = "Status must be SCHEDULED, ONGOING, COMPLETED, " +
                    "POSTPONED, or CANCELLED"
    )
    private String status;

    private Long updatedBy;

    public ExamSubjectStatusUpdateRequest() {
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