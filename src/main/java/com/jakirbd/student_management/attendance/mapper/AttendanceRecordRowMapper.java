package com.jakirbd.student_management.attendance.mapper;

import com.jakirbd.student_management.attendance.model.AttendanceRecord;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AttendanceRecordRowMapper
        implements RowMapper<AttendanceRecord> {

    @Override
    public AttendanceRecord mapRow(
            ResultSet rs,
            int rowNum
    ) throws SQLException {

        AttendanceRecord record =
                new AttendanceRecord();

        record.setAttendanceRecordId(
                rs.getLong("ATTENDANCE_RECORD_ID")
        );

        record.setAttendanceSessionId(
                rs.getLong("ATTENDANCE_SESSION_ID")
        );

        record.setEnrollmentId(
                rs.getLong("ENROLLMENT_ID")
        );

        record.setAttendanceStatus(
                rs.getString("ATTENDANCE_STATUS")
        );

        Timestamp checkInTime =
                rs.getTimestamp("CHECK_IN_TIME");

        record.setCheckInTime(
                checkInTime != null
                        ? checkInTime.toLocalDateTime()
                        : null
        );

        record.setRemarks(
                rs.getString("REMARKS")
        );

        Timestamp markedAt =
                rs.getTimestamp("MARKED_AT");

        record.setMarkedAt(
                markedAt != null
                        ? markedAt.toLocalDateTime()
                        : null
        );

        Timestamp updatedAt =
                rs.getTimestamp("UPDATED_AT");

        record.setUpdatedAt(
                updatedAt != null
                        ? updatedAt.toLocalDateTime()
                        : null
        );

        return record;
    }
}