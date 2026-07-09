package com.jakirbd.student_management.subject.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ClassSubject {

    private Long classSubjectId;
    private Long academicYearClassId;
    private Long subjectId;
    private String isMandatory;
    private BigDecimal fullMarks;
    private BigDecimal passMarks;
    private Integer displayOrder;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getClassSubjectId() {
        return classSubjectId;
    }

    public void setClassSubjectId(Long classSubjectId) {
        this.classSubjectId = classSubjectId;
    }

    public Long getAcademicYearClassId() {
        return academicYearClassId;
    }

    public void setAcademicYearClassId(Long academicYearClassId) {
        this.academicYearClassId = academicYearClassId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(String isMandatory) {
        this.isMandatory = isMandatory;
    }

    public BigDecimal getFullMarks() {
        return fullMarks;
    }

    public void setFullMarks(BigDecimal fullMarks) {
        this.fullMarks = fullMarks;
    }

    public BigDecimal getPassMarks() {
        return passMarks;
    }

    public void setPassMarks(BigDecimal passMarks) {
        this.passMarks = passMarks;
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
}
