package com.jakirbd.student_management.student.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class StudentGuardian {

    private Long guardianId;
    private Long studentId;
    private String guardianName;
    private String relationship;
    private String phone;
    private String email;
    private String occupation;
    private String isPrimary;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public StudentGuardian() {
    }

    public Long getGuardianId() {
        return guardianId;
    }

    public void setGuardianId(Long guardianId) {
        this.guardianId = guardianId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(String isPrimary) {
        this.isPrimary = isPrimary;
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

    public boolean isPrimaryGuardian() {
        return "Y".equalsIgnoreCase(isPrimary);
    }

    @Override
    public String toString() {
        return "StudentGuardian{" +
                "guardianId=" + guardianId +
                ", studentId=" + studentId +
                ", guardianName='" + guardianName + '\'' +
                ", relationship='" + relationship + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", occupation='" + occupation + '\'' +
                ", isPrimary='" + isPrimary + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StudentGuardian other)) return false;
        return Objects.equals(guardianId, other.guardianId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guardianId);
    }
}