package com.jakirbd.student_management.acss.model;

import java.time.LocalDateTime;

public class SchoolClass {

    private Long classId;
    private String className;
    private String classCode;
    private Integer displayOrder;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SchoolClass() {
    }

    public SchoolClass(
            Long classId,
            String className,
            String classCode,
            Integer displayOrder,
            String status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.classId = classId;
        this.className = className;
        this.classCode = classCode;
        this.displayOrder = displayOrder;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
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
