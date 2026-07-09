package com.jakirbd.student_management.subject.model;

import java.time.LocalDateTime;

public class TeacherSubjectAssignment {

    private Long teacherSubjectAssignmentId;
    private Long classSubjectId;
    private Long sectionShiftId;
    private Long teacherId;
    private String assignmentType;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getTeacherSubjectAssignmentId() {
        return teacherSubjectAssignmentId;
    }

    public void setTeacherSubjectAssignmentId(Long teacherSubjectAssignmentId) {
        this.teacherSubjectAssignmentId = teacherSubjectAssignmentId;
    }

    public Long getClassSubjectId() {
        return classSubjectId;
    }

    public void setClassSubjectId(Long classSubjectId) {
        this.classSubjectId = classSubjectId;
    }

    public Long getSectionShiftId() {
        return sectionShiftId;
    }

    public void setSectionShiftId(Long sectionShiftId) {
        this.sectionShiftId = sectionShiftId;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getAssignmentType() {
        return assignmentType;
    }

    public void setAssignmentType(String assignmentType) {
        this.assignmentType = assignmentType;
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
