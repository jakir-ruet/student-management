package com.jakirbd.student_management.examination.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class ExamCreateRequest {

    @NotNull(message = "Academic year ID is required")
    private Long academicYearId;

    @NotBlank(message = "Exam name is required")
    @Size(max = 100, message = "Exam name cannot exceed 100 characters")
    private String examName;

    @NotBlank(message = "Exam code is required")
    @Size(max = 30, message = "Exam code cannot exceed 30 characters")
    private String examCode;

    @NotBlank(message = "Exam type is required")
    @Pattern(
            regexp = "CLASS_TEST|QUIZ|MIDTERM|FINAL|PRACTICAL|ASSIGNMENT|OTHER",
            message = "Exam type must be CLASS_TEST, QUIZ, MIDTERM, FINAL, " +
                    "PRACTICAL, ASSIGNMENT, or OTHER"
    )
    private String examType;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Pattern(
            regexp = "DRAFT|SCHEDULED|ONGOING|COMPLETED|CANCELLED",
            message = "Status must be DRAFT, SCHEDULED, ONGOING, " +
                    "COMPLETED, or CANCELLED"
    )
    private String status;

    private Long createdBy;

    public ExamCreateRequest() {
    }

    public Long getAcademicYearId() {
        return academicYearId;
    }

    public void setAcademicYearId(Long academicYearId) {
        this.academicYearId = academicYearId;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getExamCode() {
        return examCode;
    }

    public void setExamCode(String examCode) {
        this.examCode = examCode;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}