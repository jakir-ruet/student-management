package com.jakirbd.student_management.attendance.service;

import com.jakirbd.student_management.attendance.dto.request.AttendanceSessionCreateRequest;
import com.jakirbd.student_management.attendance.dto.request.AttendanceSessionUpdateRequest;
import com.jakirbd.student_management.attendance.dto.response.AttendanceSessionResponse;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceSessionService {

    AttendanceSessionResponse createSession(
            AttendanceSessionCreateRequest request
    );

    AttendanceSessionResponse updateSession(
            Long attendanceSessionId,
            AttendanceSessionUpdateRequest request
    );

    AttendanceSessionResponse getSessionById(
            Long attendanceSessionId
    );

    List<AttendanceSessionResponse> getAllSessions();

    List<AttendanceSessionResponse> getSessionsBySectionAndDate(
            Long sectionShiftId,
            LocalDate attendanceDate
    );

    AttendanceSessionResponse changeSessionStatus(
            Long attendanceSessionId,
            String status
    );

    void deleteSessionById(Long attendanceSessionId);
}