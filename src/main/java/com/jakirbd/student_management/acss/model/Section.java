package com.jakirbd.student_management.acss.model;

import java.time.LocalDateTime;

public class Section {

    private Long sectionId;
    private Long academicYearClassId;
    private String sectionName;
    private String sectionCode;
    private Integer capacity;
    private Integer displayOrder;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Section() {
    }

    public Section(
            Long sectionId,
            Long academicYearClassId,
            String sectionName,
            String sectionCode,
            Integer capacity,
            Integer displayOrder,
            String status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.sectionId = sectionId;
        this.academicYearClassId = academicYearClassId;
        this.sectionName = sectionName;
        this.sectionCode = sectionCode;
        this.capacity = capacity;
        this.displayOrder = displayOrder;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public Long getAcademicYearClassId() {
        return academicYearClassId;
    }

    public void setAcademicYearClassId(Long academicYearClassId) {
        this.academicYearClassId = academicYearClassId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
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
