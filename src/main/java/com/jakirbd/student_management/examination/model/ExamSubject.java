package com.jakirbd.student_management.examination.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ExamSubject {

    private Long examSubjectId;
    private Long examId;
    private Long academicYearClassId;
    private Long sectionId;
    private Long subjectId;
    private LocalDate examDate;
    private String startTime;
    private String endTime;
    private Integer durationMinutes;
    private BigDecimal fullMarks;
    private BigDecimal passMarks;
    private String roomNumber;
    private String instructions;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;

    public ExamSubject() {
    }

    public ExamSubject(
            Long examSubjectId,
            Long examId,
            Long academicYearClassId,
            Long sectionId,
            Long subjectId,
            LocalDate examDate,
            String startTime,
            String endTime,
            Integer durationMinutes,
            BigDecimal fullMarks,
            BigDecimal passMarks,
            String roomNumber,
            String instructions,
            String status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Long createdBy,
            Long updatedBy
    ) {
        this.examSubjectId = examSubjectId;
        this.examId = examId;
        this.academicYearClassId = academicYearClassId;
        this.sectionId = sectionId;
        this.subjectId = subjectId;
        this.examDate = examDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMinutes = durationMinutes;
        this.fullMarks = fullMarks;
        this.passMarks = passMarks;
        this.roomNumber = roomNumber;
        this.instructions = instructions;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public Long getExamSubjectId() {
        return examSubjectId;
    }

    public void setExamSubjectId(Long examSubjectId) {
        this.examSubjectId = examSubjectId;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public Long getAcademicYearClassId() {
        return academicYearClassId;
    }

    public void setAcademicYearClassId(Long academicYearClassId) {
        this.academicYearClassId = academicYearClassId;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
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

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
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