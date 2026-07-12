package com.jakirbd.student_management.attendance.repository.impl;

import com.jakirbd.student_management.attendance.mapper.AttendanceSessionRowMapper;
import com.jakirbd.student_management.attendance.model.AttendanceSession;
import com.jakirbd.student_management.attendance.repository.AttendanceSessionRepository;
import oracle.jdbc.OracleTypes;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AttendanceSessionRepositoryImpl
        implements AttendanceSessionRepository {

    private final JdbcTemplate jdbcTemplate;

    public AttendanceSessionRepositoryImpl(
            JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long createSession(
            Long sectionShiftId,
            Long classSubjectId,
            LocalDate attendanceDate,
            String attendanceType,
            Integer periodNumber,
            Long markedByTeacherId,
            String status,
            String remarks
    ) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ATTENDANCE_SESSION_PKG.CREATE_SESSION(?, ?, ?, ?, ?, ?, ?, ?, ?)}"
            );

            cs.setLong(1, sectionShiftId);

            if (classSubjectId == null) {
                cs.setNull(2, Types.NUMERIC);
            } else {
                cs.setLong(2, classSubjectId);
            }

            if (attendanceDate == null) {
                cs.setNull(3, Types.DATE);
            } else {
                cs.setDate(
                        3,
                        Date.valueOf(attendanceDate)
                );
            }

            cs.setString(4, attendanceType);

            if (periodNumber == null) {
                cs.setNull(5, Types.INTEGER);
            } else {
                cs.setInt(5, periodNumber);
            }

            if (markedByTeacherId == null) {
                cs.setNull(6, Types.NUMERIC);
            } else {
                cs.setLong(6, markedByTeacherId);
            }

            cs.setString(7, status);
            cs.setString(8, remarks);
            cs.registerOutParameter(9, Types.NUMERIC);

            return cs;
        };

        CallableStatementCallback<Long> action = cs -> {
            cs.execute();

            Number generatedId = (Number) cs.getObject(9);

            if (generatedId == null) {
                throw new IllegalStateException(
                        "ATTENDANCE_SESSION_PKG.CREATE_SESSION "
                                + "did not return ATTENDANCE_SESSION_ID"
                );
            }

            return generatedId.longValue();
        };

        return jdbcTemplate.execute(csc, action);
    }

    @Override
    public void updateSession(
            Long attendanceSessionId,
            Long sectionShiftId,
            Long classSubjectId,
            LocalDate attendanceDate,
            String attendanceType,
            Integer periodNumber,
            Long markedByTeacherId,
            String status,
            String remarks
    ) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ATTENDANCE_SESSION_PKG.UPDATE_SESSION(?, ?, ?, ?, ?, ?, ?, ?, ?)}"
            );

            cs.setLong(1, attendanceSessionId);
            cs.setLong(2, sectionShiftId);

            if (classSubjectId == null) {
                cs.setNull(3, Types.NUMERIC);
            } else {
                cs.setLong(3, classSubjectId);
            }

            if (attendanceDate == null) {
                cs.setNull(4, Types.DATE);
            } else {
                cs.setDate(
                        4,
                        Date.valueOf(attendanceDate)
                );
            }

            cs.setString(5, attendanceType);

            if (periodNumber == null) {
                cs.setNull(6, Types.INTEGER);
            } else {
                cs.setInt(6, periodNumber);
            }

            if (markedByTeacherId == null) {
                cs.setNull(7, Types.NUMERIC);
            } else {
                cs.setLong(7, markedByTeacherId);
            }

            cs.setString(8, status);
            cs.setString(9, remarks);

            return cs;
        };

        CallableStatementCallback<Boolean> action =
                CallableStatement::execute;

        jdbcTemplate.execute(csc, action);
    }

    @Override
    public Optional<AttendanceSession> findSessionById(
            Long attendanceSessionId
    ) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ATTENDANCE_SESSION_PKG.GET_SESSION_BY_ID(?, ?)}"
            );

            cs.setLong(1, attendanceSessionId);
            cs.registerOutParameter(
                    2,
                    OracleTypes.CURSOR
            );

            return cs;
        };

        CallableStatementCallback<
                Optional<AttendanceSession>
                > action = cs -> {

            cs.execute();

            try (ResultSet rs =
                         (ResultSet) cs.getObject(2)) {

                AttendanceSessionRowMapper mapper =
                        new AttendanceSessionRowMapper();

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
    public List<AttendanceSession> findAllSessions() {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ATTENDANCE_SESSION_PKG.GET_ALL_SESSIONS(?)}"
            );

            cs.registerOutParameter(
                    1,
                    OracleTypes.CURSOR
            );

            return cs;
        };

        CallableStatementCallback<
                List<AttendanceSession>
                > action = cs -> {

            cs.execute();

            List<AttendanceSession> sessions =
                    new ArrayList<>();

            try (ResultSet rs =
                         (ResultSet) cs.getObject(1)) {

                AttendanceSessionRowMapper mapper =
                        new AttendanceSessionRowMapper();

                int rowNum = 0;

                while (rs.next()) {
                    sessions.add(
                            mapper.mapRow(rs, rowNum++)
                    );
                }
            }

            return sessions;
        };

        return jdbcTemplate.execute(csc, action);
    }

    @Override
    public List<AttendanceSession> findSessionsBySectionAndDate(
            Long sectionShiftId,
            LocalDate attendanceDate
    ) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ATTENDANCE_SESSION_PKG.GET_SESSIONS_BY_SECTION_DATE(?, ?, ?)}"
            );

            cs.setLong(1, sectionShiftId);

            if (attendanceDate == null) {
                cs.setNull(2, Types.DATE);
            } else {
                cs.setDate(
                        2,
                        Date.valueOf(attendanceDate)
                );
            }

            cs.registerOutParameter(
                    3,
                    OracleTypes.CURSOR
            );

            return cs;
        };

        CallableStatementCallback<
                List<AttendanceSession>
                > action = cs -> {

            cs.execute();

            List<AttendanceSession> sessions =
                    new ArrayList<>();

            try (ResultSet rs =
                         (ResultSet) cs.getObject(3)) {

                AttendanceSessionRowMapper mapper =
                        new AttendanceSessionRowMapper();

                int rowNum = 0;

                while (rs.next()) {
                    sessions.add(
                            mapper.mapRow(rs, rowNum++)
                    );
                }
            }

            return sessions;
        };

        return jdbcTemplate.execute(csc, action);
    }

    @Override
    public void changeSessionStatus(
            Long attendanceSessionId,
            String status
    ) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ATTENDANCE_SESSION_PKG.CHANGE_SESSION_STATUS(?, ?)}"
            );

            cs.setLong(1, attendanceSessionId);
            cs.setString(2, status);

            return cs;
        };

        CallableStatementCallback<Boolean> action =
                CallableStatement::execute;

        jdbcTemplate.execute(csc, action);
    }

    @Override
    public void deleteSessionById(
            Long attendanceSessionId
    ) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ATTENDANCE_SESSION_PKG.DELETE_SESSION(?)}"
            );

            cs.setLong(1, attendanceSessionId);

            return cs;
        };

        CallableStatementCallback<Boolean> action =
                CallableStatement::execute;

        jdbcTemplate.execute(csc, action);
    }
}