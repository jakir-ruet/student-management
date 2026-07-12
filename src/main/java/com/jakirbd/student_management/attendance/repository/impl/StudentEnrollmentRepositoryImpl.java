package com.jakirbd.student_management.attendance.repository.impl;

import com.jakirbd.student_management.attendance.mapper.StudentEnrollmentRowMapper;
import com.jakirbd.student_management.attendance.model.StudentEnrollment;
import com.jakirbd.student_management.attendance.repository.StudentEnrollmentRepository;
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
public class StudentEnrollmentRepositoryImpl
        implements StudentEnrollmentRepository {

    private final JdbcTemplate jdbcTemplate;

    public StudentEnrollmentRepositoryImpl(
            JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long createEnrollment(
            Long studentId,
            Long academicYearClassId,
            Long sectionShiftId,
            String rollNumber,
            LocalDate enrollmentDate,
            String status,
            String remarks
    ) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call STUDENT_ENROLLMENT_PKG.CREATE_ENROLLMENT(?, ?, ?, ?, ?, ?, ?, ?)}"
            );

            cs.setLong(1, studentId);
            cs.setLong(2, academicYearClassId);
            cs.setLong(3, sectionShiftId);
            cs.setString(4, rollNumber);

            if (enrollmentDate == null) {
                cs.setNull(5, Types.DATE);
            } else {
                cs.setDate(
                        5,
                        Date.valueOf(enrollmentDate)
                );
            }

            cs.setString(6, status);
            cs.setString(7, remarks);
            cs.registerOutParameter(8, Types.NUMERIC);

            return cs;
        };

        CallableStatementCallback<Long> action = cs -> {
            cs.execute();

            Number generatedId =
                    (Number) cs.getObject(8);

            if (generatedId == null) {
                throw new IllegalStateException(
                        "STUDENT_ENROLLMENT_PKG.CREATE_ENROLLMENT "
                                + "did not return ENROLLMENT_ID"
                );
            }

            return generatedId.longValue();
        };

        return jdbcTemplate.execute(csc, action);
    }

    @Override
    public void updateEnrollment(
            Long enrollmentId,
            Long academicYearClassId,
            Long sectionShiftId,
            String rollNumber,
            LocalDate enrollmentDate,
            String status,
            String remarks
    ) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call STUDENT_ENROLLMENT_PKG.UPDATE_ENROLLMENT(?, ?, ?, ?, ?, ?, ?)}"
            );

            cs.setLong(1, enrollmentId);
            cs.setLong(2, academicYearClassId);
            cs.setLong(3, sectionShiftId);
            cs.setString(4, rollNumber);

            if (enrollmentDate == null) {
                cs.setNull(5, Types.DATE);
            } else {
                cs.setDate(
                        5,
                        Date.valueOf(enrollmentDate)
                );
            }

            cs.setString(6, status);
            cs.setString(7, remarks);

            return cs;
        };

        CallableStatementCallback<Boolean> action =
                CallableStatement::execute;

        jdbcTemplate.execute(csc, action);
    }

    @Override
    public Optional<StudentEnrollment> findEnrollmentById(
            Long enrollmentId
    ) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call STUDENT_ENROLLMENT_PKG.GET_ENROLLMENT_BY_ID(?, ?)}"
            );

            cs.setLong(1, enrollmentId);
            cs.registerOutParameter(
                    2,
                    OracleTypes.CURSOR
            );

            return cs;
        };

        CallableStatementCallback<
                Optional<StudentEnrollment>
                > action = cs -> {

            cs.execute();

            try (ResultSet rs =
                         (ResultSet) cs.getObject(2)) {

                StudentEnrollmentRowMapper mapper =
                        new StudentEnrollmentRowMapper();

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
    public List<StudentEnrollment> findAllEnrollments() {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call STUDENT_ENROLLMENT_PKG.GET_ALL_ENROLLMENTS(?)}"
            );

            cs.registerOutParameter(
                    1,
                    OracleTypes.CURSOR
            );

            return cs;
        };

        CallableStatementCallback<
                List<StudentEnrollment>
                > action = cs -> {

            cs.execute();

            List<StudentEnrollment> enrollments =
                    new ArrayList<>();

            try (ResultSet rs =
                         (ResultSet) cs.getObject(1)) {

                StudentEnrollmentRowMapper mapper =
                        new StudentEnrollmentRowMapper();

                int rowNum = 0;

                while (rs.next()) {
                    enrollments.add(
                            mapper.mapRow(rs, rowNum++)
                    );
                }
            }

            return enrollments;
        };

        return jdbcTemplate.execute(csc, action);
    }

    @Override
    public List<StudentEnrollment> findEnrollmentsByStudentId(
            Long studentId
    ) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call STUDENT_ENROLLMENT_PKG.GET_ENROLLMENTS_BY_STUDENT(?, ?)}"
            );

            cs.setLong(1, studentId);
            cs.registerOutParameter(
                    2,
                    OracleTypes.CURSOR
            );

            return cs;
        };

        CallableStatementCallback<
                List<StudentEnrollment>
                > action = cs -> {

            cs.execute();

            List<StudentEnrollment> enrollments =
                    new ArrayList<>();

            try (ResultSet rs =
                         (ResultSet) cs.getObject(2)) {

                StudentEnrollmentRowMapper mapper =
                        new StudentEnrollmentRowMapper();

                int rowNum = 0;

                while (rs.next()) {
                    enrollments.add(
                            mapper.mapRow(rs, rowNum++)
                    );
                }
            }

            return enrollments;
        };

        return jdbcTemplate.execute(csc, action);
    }

    @Override
    public List<StudentEnrollment>
    findEnrollmentsBySectionShiftId(
            Long sectionShiftId,
            String status
    ) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call STUDENT_ENROLLMENT_PKG.GET_ENROLLMENTS_BY_SECTION_SHIFT(?, ?, ?)}"
            );

            cs.setLong(1, sectionShiftId);

            if (status == null || status.isBlank()) {
                cs.setNull(2, Types.VARCHAR);
            } else {
                cs.setString(2, status);
            }

            cs.registerOutParameter(
                    3,
                    OracleTypes.CURSOR
            );

            return cs;
        };

        CallableStatementCallback<
                List<StudentEnrollment>
                > action = cs -> {

            cs.execute();

            List<StudentEnrollment> enrollments =
                    new ArrayList<>();

            try (ResultSet rs =
                         (ResultSet) cs.getObject(3)) {

                StudentEnrollmentRowMapper mapper =
                        new StudentEnrollmentRowMapper();

                int rowNum = 0;

                while (rs.next()) {
                    enrollments.add(
                            mapper.mapRow(rs, rowNum++)
                    );
                }
            }

            return enrollments;
        };

        return jdbcTemplate.execute(csc, action);
    }

    @Override
    public void deleteEnrollmentById(Long enrollmentId) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call STUDENT_ENROLLMENT_PKG.DELETE_ENROLLMENT(?)}"
            );

            cs.setLong(1, enrollmentId);

            return cs;
        };

        CallableStatementCallback<Boolean> action =
                CallableStatement::execute;

        jdbcTemplate.execute(csc, action);
    }
}