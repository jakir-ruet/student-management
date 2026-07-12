package com.jakirbd.student_management.attendance.repository;

import com.jakirbd.student_management.attendance.dto.response.AttendanceRecordResponse;
import com.jakirbd.student_management.attendance.model.AttendanceRecord;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AttendanceRecordRepository {

    Long createRecord(
            Long attendanceSessionId,
            Long enrollmentId,
            String attendanceStatus,
            LocalDateTime checkInTime,
            String remarks
    );

    void updateRecord(
            Long attendanceRecordId,
            String attendanceStatus,
            LocalDateTime checkInTime,
            String remarks
    );

    Optional<AttendanceRecord> findRecordById(
            Long attendanceRecordId
    );

    List<AttendanceRecordResponse> findRecordsBySessionId(
            Long attendanceSessionId
    );

    List<AttendanceRecord> findRecordsByEnrollmentId(
            Long enrollmentId
    );

    void deleteRecordById(Long attendanceRecordId);
}