package com.jakirbd.student_management.subject.repository.impl;

import com.jakirbd.student_management.subject.dto.response.TeacherSubjectAssignmentResponse;
import com.jakirbd.student_management.subject.mapper.TeacherSubjectAssignmentResponseRowMapper;
import com.jakirbd.student_management.subject.repository.TeacherSubjectAssignmentRepository;
import oracle.jdbc.OracleTypes;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TeacherSubjectAssignmentRepositoryImpl
        implements TeacherSubjectAssignmentRepository {

    private final JdbcTemplate jdbcTemplate;

    public TeacherSubjectAssignmentRepositoryImpl(
            JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long createAssignment(
            Long classSubjectId,
            Long sectionShiftId,
            Long teacherId,
            String assignmentType,
            String status
    ) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call TEACHER_SUBJECT_ASSIGNMENT_PKG.CREATE_ASSIGNMENT(?, ?, ?, ?, ?, ?)}"
            );

            cs.setLong(1, classSubjectId);
            cs.setLong(2, sectionShiftId);
            cs.setLong(3, teacherId);
            cs.setString(4, assignmentType);
            cs.setString(5, status);
            cs.registerOutParameter(6, Types.NUMERIC);

            return cs;
        };

        CallableStatementCallback<Long> action = cs -> {
            cs.execute();
            return cs.getLong(6);
        };

        return jdbcTemplate.execute(csc, action);
    }

    @Override
    public void updateAssignment(
            Long assignmentId,
            Long classSubjectId,
            Long sectionShiftId,
            Long teacherId,
            String assignmentType,
            String status
    ) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call TEACHER_SUBJECT_ASSIGNMENT_PKG.UPDATE_ASSIGNMENT(?, ?, ?, ?, ?, ?)}"
            );

            cs.setLong(1, assignmentId);
            cs.setLong(2, classSubjectId);
            cs.setLong(3, sectionShiftId);
            cs.setLong(4, teacherId);
            cs.setString(5, assignmentType);
            cs.setString(6, status);

            return cs;
        };

        CallableStatementCallback<Boolean> action =
                CallableStatement::execute;

        jdbcTemplate.execute(csc, action);
    }

    @Override
    public Optional<TeacherSubjectAssignmentResponse> findAssignmentById(
            Long assignmentId
    ) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call TEACHER_SUBJECT_ASSIGNMENT_PKG.GET_ASSIGNMENT_BY_ID(?, ?)}"
            );

            cs.setLong(1, assignmentId);
            cs.registerOutParameter(2, OracleTypes.CURSOR);

            return cs;
        };

        CallableStatementCallback<
                Optional<TeacherSubjectAssignmentResponse>
                > action = cs -> {

            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                TeacherSubjectAssignmentResponseRowMapper mapper =
                        new TeacherSubjectAssignmentResponseRowMapper();

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
    public List<TeacherSubjectAssignmentResponse> findAllAssignments() {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call TEACHER_SUBJECT_ASSIGNMENT_PKG.GET_ALL_ASSIGNMENTS(?)}"
            );

            cs.registerOutParameter(1, OracleTypes.CURSOR);

            return cs;
        };

        CallableStatementCallback<
                List<TeacherSubjectAssignmentResponse>
                > action = cs -> {

            cs.execute();

            List<TeacherSubjectAssignmentResponse> assignments =
                    new ArrayList<>();

            try (ResultSet rs = (ResultSet) cs.getObject(1)) {
                TeacherSubjectAssignmentResponseRowMapper mapper =
                        new TeacherSubjectAssignmentResponseRowMapper();

                int rowNum = 0;

                while (rs.next()) {
                    assignments.add(
                            mapper.mapRow(rs, rowNum++)
                    );
                }
            }

            return assignments;
        };

        return jdbcTemplate.execute(csc, action);
    }

    @Override
    public List<TeacherSubjectAssignmentResponse> findAssignmentsByTeacherId(
            Long teacherId
    ) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call TEACHER_SUBJECT_ASSIGNMENT_PKG.GET_ASSIGNMENTS_BY_TEACHER(?, ?)}"
            );

            cs.setLong(1, teacherId);
            cs.registerOutParameter(2, OracleTypes.CURSOR);

            return cs;
        };

        CallableStatementCallback<
                List<TeacherSubjectAssignmentResponse>
                > action = cs -> {

            cs.execute();

            List<TeacherSubjectAssignmentResponse> assignments =
                    new ArrayList<>();

            try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                TeacherSubjectAssignmentResponseRowMapper mapper =
                        new TeacherSubjectAssignmentResponseRowMapper();

                int rowNum = 0;

                while (rs.next()) {
                    assignments.add(
                            mapper.mapRow(rs, rowNum++)
                    );
                }
            }

            return assignments;
        };

        return jdbcTemplate.execute(csc, action);
    }

    @Override
    public void deleteAssignmentById(Long assignmentId) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call TEACHER_SUBJECT_ASSIGNMENT_PKG.DELETE_ASSIGNMENT(?)}"
            );

            cs.setLong(1, assignmentId);

            return cs;
        };

        CallableStatementCallback<Boolean> action =
                CallableStatement::execute;

        jdbcTemplate.execute(csc, action);
    }
}