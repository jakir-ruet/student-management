package com.jakirbd.student_management.subject.repository.impl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.jakirbd.student_management.common.exception.OracleExceptionTranslator;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.jakirbd.student_management.subject.mapper.SubjectRowMapper;
import com.jakirbd.student_management.subject.model.Subject;
import com.jakirbd.student_management.subject.repository.SubjectRepository;

import oracle.jdbc.OracleTypes;

@Repository
public class SubjectRepositoryImpl implements SubjectRepository {

    private final JdbcTemplate jdbcTemplate;

    public SubjectRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long createSubject(
            String subjectName,
            String subjectCode,
            String subjectType,
            String description,
            Integer displayOrder,
            String status
    ) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call SUBJECT_PKG.CREATE_SUBJECT(?, ?, ?, ?, ?, ?, ?)}"
            );

            cs.setString(1, subjectName);
            cs.setString(2, subjectCode);
            cs.setString(3, subjectType);
            cs.setString(4, description);

            if (displayOrder == null) {
                cs.setNull(5, Types.INTEGER);
            } else {
                cs.setInt(5, displayOrder);
            }

            cs.setString(6, status);
            cs.registerOutParameter(7, Types.NUMERIC);

            return cs;
        };

        CallableStatementCallback<Long> action = cs -> {
            cs.execute();

            Number generatedId = (Number) cs.getObject(7);

            if (generatedId == null) {
                throw new IllegalStateException(
                        "SUBJECT_PKG.CREATE_SUBJECT did not return SUBJECT_ID"
                );
            }

            return generatedId.longValue();
        };

        return jdbcTemplate.execute(csc, action);
    }

    @Override
    public void updateSubject(
            Long subjectId,
            String subjectName,
            String subjectCode,
            String subjectType,
            String description,
            Integer displayOrder,
            String status
    ) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call SUBJECT_PKG.UPDATE_SUBJECT(?, ?, ?, ?, ?, ?, ?)}"
            );

            cs.setLong(1, subjectId);
            cs.setString(2, subjectName);
            cs.setString(3, subjectCode);
            cs.setString(4, subjectType);
            cs.setString(5, description);

            if (displayOrder == null) {
                cs.setNull(6, Types.INTEGER);
            } else {
                cs.setInt(6, displayOrder);
            }

            cs.setString(7, status);

            return cs;
        };

        CallableStatementCallback<Boolean> action = CallableStatement::execute;

        jdbcTemplate.execute(csc, action);
    }

    @Override
    public Optional<Subject> findSubjectById(Long subjectId) {
        try {
            CallableStatementCreator csc = connection -> {
                CallableStatement cs = connection.prepareCall(
                        "{call SUBJECT_PKG.GET_SUBJECT_BY_ID(?, ?)}"
                );

                cs.setLong(1, subjectId);
                cs.registerOutParameter(2, OracleTypes.CURSOR);

                return cs;
            };

            CallableStatementCallback<Optional<Subject>> action = cs -> {
                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                    SubjectRowMapper mapper = new SubjectRowMapper();

                    if (rs.next()) {
                        return Optional.of(
                                mapper.mapRow(rs, 0)
                        );
                    }

                    return Optional.empty();
                }
            };

            return jdbcTemplate.execute(csc, action);

        } catch (DataAccessException exception) {
            throw OracleExceptionTranslator.translate(exception);
        }
    }

    @Override
    public List<Subject> findAllSubjects() {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call SUBJECT_PKG.GET_ALL_SUBJECTS(?)}"
            );

            cs.registerOutParameter(1, OracleTypes.CURSOR);

            return cs;
        };

        CallableStatementCallback<List<Subject>> action = cs -> {
            cs.execute();

            List<Subject> subjects = new ArrayList<>();

            try (ResultSet rs = (ResultSet) cs.getObject(1)) {
                SubjectRowMapper mapper = new SubjectRowMapper();

                int rowNum = 0;
                while (rs.next()) {
                    subjects.add(mapper.mapRow(rs, rowNum++));
                }
            }

            return subjects;
        };

        return jdbcTemplate.execute(csc, action);
    }

    @Override
    public void deleteSubjectById(Long subjectId) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call SUBJECT_PKG.DELETE_SUBJECT(?)}"
            );

            cs.setLong(1, subjectId);

            return cs;
        };

        CallableStatementCallback<Boolean> action = CallableStatement::execute;

        jdbcTemplate.execute(csc, action);
    }
}
