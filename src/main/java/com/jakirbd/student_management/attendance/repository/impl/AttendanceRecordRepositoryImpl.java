package com.jakirbd.student_management.attendance.repository.impl;

import com.jakirbd.student_management.attendance.dto.response.AttendanceRecordResponse;
import com.jakirbd.student_management.attendance.mapper.AttendanceRecordResponseRowMapper;
import com.jakirbd.student_management.attendance.mapper.AttendanceRecordRowMapper;
import com.jakirbd.student_management.attendance.model.AttendanceRecord;
import com.jakirbd.student_management.attendance.repository.AttendanceRecordRepository;
import oracle.jdbc.OracleTypes;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AttendanceRecordRepositoryImpl
        implements AttendanceRecordRepository {

    private final JdbcTemplate jdbcTemplate;

    public AttendanceRecordRepositoryImpl(
            JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long createRecord(
            Long attendanceSessionId,
            Long enrollmentId,
            String attendanceStatus,
            LocalDateTime checkInTime,
            String remarks
    ) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ATTENDANCE_RECORD_PKG.CREATE_RECORD(?, ?, ?, ?, ?, ?)}"
            );

            cs.setLong(1, attendanceSessionId);
            cs.setLong(2, enrollmentId);
            cs.setString(3, attendanceStatus);

            if (checkInTime == null) {
                cs.setNull(4, Types.TIMESTAMP);
            } else {
                cs.setTimestamp(
                        4,
                        Timestamp.valueOf(checkInTime)
                );
            }

            cs.setString(5, remarks);
            cs.registerOutParameter(6, Types.NUMERIC);

            return cs;
        };

        CallableStatementCallback<Long> action = cs -> {
            cs.execute();

            Number generatedId = (Number) cs.getObject(6);

            if (generatedId == null) {
                throw new IllegalStateException(
                        "ATTENDANCE_RECORD_PKG.CREATE_RECORD "
                                + "did not return ATTENDANCE_RECORD_ID"
                );
            }

            return generatedId.longValue();
        };

        return jdbcTemplate.execute(csc, action);
    }

    @Override
    public void updateRecord(
            Long attendanceRecordId,
            String attendanceStatus,
            LocalDateTime checkInTime,
            String remarks
    ) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ATTENDANCE_RECORD_PKG.UPDATE_RECORD(?, ?, ?, ?)}"
            );

            cs.setLong(1, attendanceRecordId);
            cs.setString(2, attendanceStatus);

            if (checkInTime == null) {
                cs.setNull(3, Types.TIMESTAMP);
            } else {
                cs.setTimestamp(
                        3,
                        Timestamp.valueOf(checkInTime)
                );
            }

            cs.setString(4, remarks);

            return cs;
        };

        CallableStatementCallback<Boolean> action =
                CallableStatement::execute;

        jdbcTemplate.execute(csc, action);
    }

    @Override
    public Optional<AttendanceRecord> findRecordById(
            Long attendanceRecordId
    ) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ATTENDANCE_RECORD_PKG.GET_RECORD_BY_ID(?, ?)}"
            );

            cs.setLong(1, attendanceRecordId);
            cs.registerOutParameter(
                    2,
                    OracleTypes.CURSOR
            );

            return cs;
        };

        CallableStatementCallback<
                Optional<AttendanceRecord>
                > action = cs -> {

            cs.execute();

            try (ResultSet rs =
                         (ResultSet) cs.getObject(2)) {

                AttendanceRecordRowMapper mapper =
                        new AttendanceRecordRowMapper();

                if (rs.next()) {
                    return Optional.of(
                            mapper.mapRow(rs, 0)
                    );
                }

                return Optional.empty();
            }
        };

        return jdbcTemplate.execute(csc, action);
    }

    @Override
    public List<AttendanceRecordResponse> findRecordsBySessionId(
            Long attendanceSessionId
    ) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ATTENDANCE_RECORD_PKG.GET_RECORDS_BY_SESSION(?, ?)}"
            );

            cs.setLong(1, attendanceSessionId);
            cs.registerOutParameter(
                    2,
                    OracleTypes.CURSOR
            );

            return cs;
        };

        CallableStatementCallback<
                List<AttendanceRecordResponse>
                > action = cs -> {

            cs.execute();

            List<AttendanceRecordResponse> records =
                    new ArrayList<>();

            try (ResultSet rs =
                         (ResultSet) cs.getObject(2)) {

                AttendanceRecordResponseRowMapper mapper =
                        new AttendanceRecordResponseRowMapper();

                int rowNum = 0;

                while (rs.next()) {
                    records.add(
                            mapper.mapRow(rs, rowNum++)
                    );
                }
            }

            return records;
        };

        return jdbcTemplate.execute(csc, action);
    }

    @Override
    public List<AttendanceRecord> findRecordsByEnrollmentId(
            Long enrollmentId
    ) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ATTENDANCE_RECORD_PKG.GET_RECORDS_BY_ENROLLMENT(?, ?)}"
            );

            cs.setLong(1, enrollmentId);
            cs.registerOutParameter(
                    2,
                    OracleTypes.CURSOR
            );

            return cs;
        };

        CallableStatementCallback<
                List<AttendanceRecord>
                > action = cs -> {

            cs.execute();

            List<AttendanceRecord> records =
                    new ArrayList<>();

            try (ResultSet rs =
                         (ResultSet) cs.getObject(2)) {

                AttendanceRecordRowMapper mapper =
                        new AttendanceRecordRowMapper();

                int rowNum = 0;

                while (rs.next()) {
                    records.add(
                            mapper.mapRow(rs, rowNum++)
                    );
                }
            }

            return records;
        };

        return jdbcTemplate.execute(csc, action);
    }

    @Override
    public void deleteRecordById(
            Long attendanceRecordId
    ) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ATTENDANCE_RECORD_PKG.DELETE_RECORD(?)}"
            );

            cs.setLong(1, attendanceRecordId);

            return cs;
        };

        CallableStatementCallback<Boolean> action =
                CallableStatement::execute;

        jdbcTemplate.execute(csc, action);
    }
}