package com.jakirbd.student_management.attendance.repository;

import com.jakirbd.student_management.attendance.model.AttendanceSession;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceSessionRepository {

    Long createSession(
            Long sectionShiftId,
            Long classSubjectId,
            LocalDate attendanceDate,
            String attendanceType,
            Integer periodNumber,
            Long markedByTeacherId,
            String status,
            String remarks
    );

    void updateSession(
            Long attendanceSessionId,
            Long sectionShiftId,
            Long classSubjectId,
            LocalDate attendanceDate,
            String attendanceType,
            Integer periodNumber,
            Long markedByTeacherId,
            String status,
            String remarks
    );

    Optional<AttendanceSession> findSessionById(
            Long attendanceSessionId
    );

    List<AttendanceSession> findAllSessions();

    List<AttendanceSession> findSessionsBySectionAndDate(
            Long sectionShiftId,
            LocalDate attendanceDate
    );

    void changeSessionStatus(
            Long attendanceSessionId,
            String status
    );

    void deleteSessionById(Long attendanceSessionId);
}