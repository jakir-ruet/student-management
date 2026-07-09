package com.jakirbd.student_management.acss.model;

import java.time.LocalDateTime;

public class AcademicYearClass {

    private Long academicYearClassId;
    private Long academicYearId;
    private Long classId;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AcademicYearClass() {
    }

    public AcademicYearClass(
            Long academicYearClassId,
            Long academicYearId,
            Long classId,
            String status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.academicYearClassId = academicYearClassId;
        this.academicYearId = academicYearId;
        this.classId = classId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getAcademicYearClassId() {
        return academicYearClassId;
    }

    public void setAcademicYearClassId(Long academicYearClassId) {
        this.academicYearClassId = academicYearClassId;
    }

    public Long getAcademicYearId() {
        return academicYearId;
    }

    public void setAcademicYearId(Long academicYearId) {
        this.academicYearId = academicYearId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
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
