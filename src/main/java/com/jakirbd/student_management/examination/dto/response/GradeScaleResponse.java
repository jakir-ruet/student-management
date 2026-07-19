package com.jakirbd.student_management.examination.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GradeScaleResponse {

    private Long gradeScaleId;
    private String gradeName;
    private String letterGrade;
    private BigDecimal minPercentage;
    private BigDecimal maxPercentage;
    private BigDecimal gradePoint;
    private String description;
    private Integer displayOrder;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;

    public GradeScaleResponse() {
    }

    public GradeScaleResponse(
            Long gradeScaleId,
            String gradeName,
            String letterGrade,
            BigDecimal minPercentage,
            BigDecimal maxPercentage,
            BigDecimal gradePoint,
            String description,
            Integer displayOrder,
            String status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Long createdBy,
            Long updatedBy
    ) {
        this.gradeScaleId = gradeScaleId;
        this.gradeName = gradeName;
        this.letterGrade = letterGrade;
        this.minPercentage = minPercentage;
        this.maxPercentage = maxPercentage;
        this.gradePoint = gradePoint;
        this.description = description;
        this.displayOrder = displayOrder;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public Long getGradeScaleId() {
        return gradeScaleId;
    }

    public void setGradeScaleId(Long gradeScaleId) {
        this.gradeScaleId = gradeScaleId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
}