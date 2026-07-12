package com.jakirbd.student_management.attendance.mapper;

import com.jakirbd.student_management.attendance.model.AttendanceSession;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AttendanceSessionRowMapper
        implements RowMapper<AttendanceSession> {

    @Override
    public AttendanceSession mapRow(
            ResultSet rs,
            int rowNum
    ) throws SQLException {

        AttendanceSession session =
                new AttendanceSession();

        session.setAttendanceSessionId(
                rs.getLong("ATTENDANCE_SESSION_ID")
        );

        session.setSectionShiftId(
                rs.getLong("SECTION_SHIFT_ID")
        );

        long classSubjectId =
                rs.getLong("CLASS_SUBJECT_ID");

        session.setClassSubjectId(
                rs.wasNull() ? null : classSubjectId
        );

        Date attendanceDate =
                rs.getDate("ATTENDANCE_DATE");

        session.setAttendanceDate(
                attendanceDate != null
                        ? attendanceDate.toLocalDate()
                        : null
        );

        session.setAttendanceType(
                rs.getString("ATTENDANCE_TYPE")
        );

        int periodNumber =
                rs.getInt("PERIOD_NUMBER");

        session.setPeriodNumber(
                rs.wasNull() ? null : periodNumber
        );

        long markedByTeacherId =
                rs.getLong("MARKED_BY_TEACHER_ID");

        session.setMarkedByTeacherId(
                rs.wasNull() ? null : markedByTeacherId
        );

        session.setStatus(
                rs.getString("STATUS")
        );

        session.setRemarks(
                rs.getString("REMARKS")
        );

        Timestamp createdAt =
                rs.getTimestamp("CREATED_AT");

        session.setCreatedAt(
                createdAt != null
                        ? createdAt.toLocalDateTime()
                        : null
        );

        Timestamp updatedAt =
                rs.getTimestamp("UPDATED_AT");

        session.setUpdatedAt(
                updatedAt != null
                        ? updatedAt.toLocalDateTime()
                        : null
        );

        return session;
    }
}