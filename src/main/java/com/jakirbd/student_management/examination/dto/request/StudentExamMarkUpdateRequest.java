package com.jakirbd.student_management.examination.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class StudentExamMarkUpdateRequest {

    @DecimalMin(
            value = "0.00",
            message = "Obtained marks cannot be negative"
    )
    private BigDecimal obtainedMarks;

    @NotBlank(message = "Attendance status is required")
    @Pattern(
            regexp = "PRESENT|ABSENT|EXCUSED",
            message = "Attendance status must be PRESENT, ABSENT, or EXCUSED"
    )
    private String attendanceStatus;

    @Size(max = 500, message = "Remarks cannot exceed 500 characters")
    private String remarks;

    private Long updatedBy;

    public StudentExamMarkUpdateRequest() {
    }

    public BigDecimal getObtainedMarks() {
        return obtainedMarks;
    }

    public void setObtainedMarks(BigDecimal obtainedMarks) {
        this.obtainedMarks = obtainedMarks;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
}