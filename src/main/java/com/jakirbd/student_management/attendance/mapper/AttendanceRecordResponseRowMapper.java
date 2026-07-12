package com.jakirbd.student_management.attendance.mapper;

import com.jakirbd.student_management.attendance.dto.response.AttendanceRecordResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AttendanceRecordResponseRowMapper
        implements RowMapper<AttendanceRecordResponse> {

    @Override
    public AttendanceRecordResponse mapRow(
            ResultSet rs,
            int rowNum
    ) throws SQLException {

        AttendanceRecordResponse response =
                new AttendanceRecordResponse();

        response.setAttendanceRecordId(
                rs.getLong("ATTENDANCE_RECORD_ID")
        );

        response.setAttendanceSessionId(
                rs.getLong("ATTENDANCE_SESSION_ID")
        );

        response.setEnrollmentId(
                rs.getLong("ENROLLMENT_ID")
        );

        long studentId = rs.getLong("STUDENT_ID");

        response.setStudentId(
                rs.wasNull() ? null : studentId
        );

        response.setRollNumber(
                rs.getString("ROLL_NUMBER")
        );

        response.setAttendanceStatus(
                rs.getString("ATTENDANCE_STATUS")
        );

        Timestamp checkInTime =
                rs.getTimestamp("CHECK_IN_TIME");

        response.setCheckInTime(
                checkInTime != null
                        ? checkInTime.toLocalDateTime()
                        : null
        );

        response.setRemarks(
                rs.getString("REMARKS")
        );

        Timestamp markedAt =
                rs.getTimestamp("MARKED_AT");

        response.setMarkedAt(
                markedAt != null
                        ? markedAt.toLocalDateTime()
                        : null
        );

        Timestamp updatedAt =
                rs.getTimestamp("UPDATED_AT");

        response.setUpdatedAt(
                updatedAt != null
                        ? updatedAt.toLocalDateTime()
                        : null
        );

        return response;
    }
}