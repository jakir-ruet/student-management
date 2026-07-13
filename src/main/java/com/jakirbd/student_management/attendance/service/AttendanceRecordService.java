package com.jakirbd.student_management.attendance.service;

import com.jakirbd.student_management.attendance.dto.request.AttendanceRecordCreateRequest;
import com.jakirbd.student_management.attendance.dto.request.AttendanceRecordUpdateRequest;
import com.jakirbd.student_management.attendance.dto.response.AttendanceRecordResponse;

import java.util.List;

public interface AttendanceRecordService {

    AttendanceRecordResponse createRecord(
            AttendanceRecordCreateRequest request
    );

    AttendanceRecordResponse updateRecord(
            Long attendanceRecordId,
            AttendanceRecordUpdateRequest request
    );

    AttendanceRecordResponse getRecordById(
            Long attendanceRecordId
    );

    List<AttendanceRecordResponse> getRecordsBySessionId(
            Long attendanceSessionId
    );

    List<AttendanceRecordResponse> getRecordsByEnrollmentId(
            Long enrollmentId
    );

    void deleteRecordById(Long attendanceRecordId);
}