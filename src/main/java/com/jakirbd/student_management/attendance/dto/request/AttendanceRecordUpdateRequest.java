package com.jakirbd.student_management.attendance.dto.request;

import java.time.LocalDateTime;

public class AttendanceRecordUpdateRequest {

    private String attendanceStatus;
    private LocalDateTime checkInTime;
    private String remarks;

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}