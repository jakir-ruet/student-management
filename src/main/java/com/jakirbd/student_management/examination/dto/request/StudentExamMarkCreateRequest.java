package com.jakirbd.student_management.examination.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class StudentExamMarkCreateRequest {

    @NotNull(message = "Exam subject ID is required")
    private Long examSubjectId;

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @DecimalMin(
            value = "0.00",
            message = "Obtained marks cannot be negative"
    )
    private BigDecimal obtainedMarks;

    @Pattern(
            regexp = "PRESENT|ABSENT|EXCUSED",
            message = "Attendance status must be PRESENT, ABSENT, or EXCUSED"
    )
    private String attendanceStatus;

    @Size(max = 500, message = "Remarks cannot exceed 500 characters")
    private String remarks;

    private Long createdBy;

    public StudentExamMarkCreateRequest() {
    }

    public Long getExamSubjectId() {
        return examSubjectId;
    }

    public void setExamSubjectId(Long examSubjectId) {
        this.examSubjectId = examSubjectId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
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

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}