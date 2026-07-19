package com.jakirbd.student_management.examination.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class StudentExamResultResponse {

    private Long studentExamMarkId;
    private Long examSubjectId;
    private Long examId;
    private Long subjectId;
    private BigDecimal fullMarks;
    private BigDecimal passMarks;
    private Long studentId;
    private BigDecimal obtainedMarks;
    private BigDecimal percentage;
    private String letterGrade;
    private BigDecimal gradePoint;
    private String resultStatus;
    private String attendanceStatus;
    private String remarks;
    private String publishedFlag;
    private LocalDateTime markedAt;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;

    public StudentExamResultResponse() {
    }

    public StudentExamResultResponse(
            Long studentExamMarkId,
            Long examSubjectId,
            Long examId,
            Long subjectId,
            BigDecimal fullMarks,
            BigDecimal passMarks,
            Long studentId,
            BigDecimal obtainedMarks,
            BigDecimal percentage,
            String letterGrade,
            BigDecimal gradePoint,
            String resultStatus,
            String attendanceStatus,
            String remarks,
            String publishedFlag,
            LocalDateTime markedAt,
            LocalDateTime publishedAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Long createdBy,
            Long updatedBy
    ) {
        this.studentExamMarkId = studentExamMarkId;
        this.examSubjectId = examSubjectId;
        this.examId = examId;
        this.subjectId = subjectId;
        this.fullMarks = fullMarks;
        this.passMarks = passMarks;
        this.studentId = studentId;
        this.obtainedMarks = obtainedMarks;
        this.percentage = percentage;
        this.letterGrade = letterGrade;
        this.gradePoint = gradePoint;
        this.resultStatus = resultStatus;
        this.attendanceStatus = attendanceStatus;
        this.remarks = remarks;
        this.publishedFlag = publishedFlag;
        this.markedAt = markedAt;
        this.publishedAt = publishedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public Long getStudentExamMarkId() {
        return studentExamMarkId;
    }

    public void setStudentExamMarkId(Long studentExamMarkId) {
        this.studentExamMarkId = studentExamMarkId;
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

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
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

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public BigDecimal getObtainedMarks() {
        return obtainedMarks;
    }

    public void setObtainedMarks(BigDecimal obtainedMarks) {
        this.obtainedMarks = obtainedMarks;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public void setLetterGrade(String letterGrade) {
        this.letterGrade = letterGrade;
    }

    public BigDecimal getGradePoint() {
        return gradePoint;
    }

    public void setGradePoint(BigDecimal gradePoint) {
        this.gradePoint = gradePoint;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPublishedFlag() {
        return publishedFlag;
    }

    public void setPublishedFlag(String publishedFlag) {
        this.publishedFlag = publishedFlag;
    }

    public LocalDateTime getMarkedAt() {
        return markedAt;
    }

    public void setMarkedAt(LocalDateTime markedAt) {
        this.markedAt = markedAt;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
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