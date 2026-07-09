package com.jakirbd.student_management.acss.model;

import java.time.LocalDateTime;

public class SectionShift {

    private Long sectionShiftId;
    private Long sectionId;
    private Long shiftId;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SectionShift() {
    }

    public SectionShift(
            Long sectionShiftId,
            Long sectionId,
            Long shiftId,
            String status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.sectionShiftId = sectionShiftId;
        this.sectionId = sectionId;
        this.shiftId = shiftId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getSectionShiftId() {
        return sectionShiftId;
    }

    public void setSectionShiftId(Long sectionShiftId) {
        this.sectionShiftId = sectionShiftId;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public Long getShiftId() {
        return shiftId;
    }

    public void setShiftId(Long shiftId) {
        this.shiftId = shiftId;
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
