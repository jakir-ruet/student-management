package com.jakirbd.student_management.attendance.dto.request;

import java.time.LocalDate;

public class AttendanceSessionCreateRequest {

    private Long sectionShiftId;
    private Long classSubjectId;
    private LocalDate attendanceDate;
    private String attendanceType;
    private Integer periodNumber;
    private Long markedByTeacherId;
    private String status;
    private String remarks;

    public Long getSectionShiftId() {
        return sectionShiftId;
    }

    public void setSectionShiftId(Long sectionShiftId) {
        this.sectionShiftId = sectionShiftId;
    }

    public Long getClassSubjectId() {
        return classSubjectId;
    }

    public void setClassSubjectId(Long classSubjectId) {
        this.classSubjectId = classSubjectId;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }

    public Integer getPeriodNumber() {
        return periodNumber;
    }

    public void setPeriodNumber(Integer periodNumber) {
        this.periodNumber = periodNumber;
    }

    public Long getMarkedByTeacherId() {
        return markedByTeacherId;
    }

    public void setMarkedByTeacherId(Long markedByTeacherId) {
        this.markedByTeacherId = markedByTeacherId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}