package com.jakirbd.student_management.examination.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExamSubjectCreateRequest {

    @NotNull(message = "Exam ID is required")
    private Long examId;

    @NotNull(message = "Academic year class ID is required")
    private Long academicYearClassId;

    private Long sectionId;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    @NotNull(message = "Exam date is required")
    private LocalDate examDate;

    @Pattern(
            regexp = "^([01]\\d|2[0-3]):[0-5]\\d$",
            message = "Start time must use HH:mm format"
    )
    private String startTime;

    @Pattern(
            regexp = "^([01]\\d|2[0-3]):[0-5]\\d$",
            message = "End time must use HH:mm format"
    )
    private String endTime;

    @Min(value = 1, message = "Duration must be greater than zero")
    private Integer durationMinutes;

    @NotNull(message = "Full marks are required")
    @DecimalMin(
            value = "0.01",
            message = "Full marks must be greater than zero"
    )
    private BigDecimal fullMarks;

    @NotNull(message = "Pass marks are required")
    @DecimalMin(
            value = "0.00",
            message = "Pass marks cannot be negative"
    )
    private BigDecimal passMarks;

    @Size(max = 30, message = "Room number cannot exceed 30 characters")
    private String roomNumber;

    @Size(max = 1000, message = "Instructions cannot exceed 1000 characters")
    private String instructions;

    @Pattern(
            regexp = "SCHEDULED|ONGOING|COMPLETED|POSTPONED|CANCELLED",
            message = "Status must be SCHEDULED, ONGOING, COMPLETED, " +
                    "POSTPONED, or CANCELLED"
    )
    private String status;

    private Long createdBy;

    public ExamSubjectCreateRequest() {
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

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}