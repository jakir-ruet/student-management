package com.jakirbd.student_management.examination.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class GradeScaleUpdateRequest {

    @NotBlank(message = "Grade name is required")
    @Size(max = 20, message = "Grade name cannot exceed 20 characters")
    private String gradeName;

    @NotBlank(message = "Letter grade is required")
    @Size(max = 5, message = "Letter grade cannot exceed 5 characters")
    private String letterGrade;

    @NotNull(message = "Minimum percentage is required")
    @DecimalMin(
            value = "0.00",
            message = "Minimum percentage cannot be less than zero"
    )
    @DecimalMax(
            value = "100.00",
            message = "Minimum percentage cannot exceed 100"
    )
    private BigDecimal minPercentage;

    @NotNull(message = "Maximum percentage is required")
    @DecimalMin(
            value = "0.00",
            message = "Maximum percentage cannot be less than zero"
    )
    @DecimalMax(
            value = "100.00",
            message = "Maximum percentage cannot exceed 100"
    )
    private BigDecimal maxPercentage;

    @NotNull(message = "Grade point is required")
    @DecimalMin(
            value = "0.00",
            message = "Grade point cannot be negative"
    )
    private BigDecimal gradePoint;

    @Size(max = 250, message = "Description cannot exceed 250 characters")
    private String description;

    @NotNull(message = "Display order is required")
    @Min(value = 1, message = "Display order must be at least 1")
    private Integer displayOrder;

    @NotBlank(message = "Status is required")
    @Pattern(
            regexp = "ACTIVE|INACTIVE",
            message = "Status must be ACTIVE or INACTIVE"
    )
    private String status;

    private Long updatedBy;

    public GradeScaleUpdateRequest() {
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public void setLetterGrade(String letterGrade) {
        this.letterGrade = letterGrade;
    }

    public BigDecimal getMinPercentage() {
        return minPercentage;
    }

    public void setMinPercentage(BigDecimal minPercentage) {
        this.minPercentage = minPercentage;
    }

    public BigDecimal getMaxPercentage() {
        return maxPercentage;
    }

    public void setMaxPercentage(BigDecimal maxPercentage) {
        this.maxPercentage = maxPercentage;
    }

    public BigDecimal getGradePoint() {
        return gradePoint;
    }

    public void setGradePoint(BigDecimal gradePoint) {
        this.gradePoint = gradePoint;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
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