package com.jakirbd.student_management.subject.repository.impl;

import com.jakirbd.student_management.subject.dto.response.ClassSubjectResponse;
import com.jakirbd.student_management.subject.mapper.ClassSubjectResponseRowMapper;
import com.jakirbd.student_management.subject.repository.ClassSubjectRepository;
import oracle.jdbc.OracleTypes;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ClassSubjectRepositoryImpl implements ClassSubjectRepository {

    private final JdbcTemplate jdbcTemplate;

    public ClassSubjectRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long createClassSubject(
            Long academicYearClassId,
            Long subjectId,
            String isMandatory,
            BigDecimal fullMarks,
            BigDecimal passMarks,
            Integer displayOrder,
            String status
    ) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call CLASS_SUBJECT_PKG.CREATE_CLASS_SUBJECT(?, ?, ?, ?, ?, ?, ?, ?)}"
            );

            cs.setLong(1, academicYearClassId);
            cs.setLong(2, subjectId);
            cs.setString(3, isMandatory);
            cs.setBigDecimal(4, fullMarks);
            cs.setBigDecimal(5, passMarks);

            if (displayOrder == null) {
                cs.setNull(6, Types.INTEGER);
            } else {
                cs.setInt(6, displayOrder);
            }

            cs.setString(7, status);
            cs.registerOutParameter(8, Types.NUMERIC);

            return cs;
        };

        CallableStatementCallback<Long> action = cs -> {
            cs.execute();
            return cs.getLong(8);
        };

        return jdbcTemplate.execute(csc, action);
    }

    @Override
    public void updateClassSubject(
            Long classSubjectId,
            Long academicYearClassId,
            Long subjectId,
            String isMandatory,
            BigDecimal fullMarks,
            BigDecimal passMarks,
            Integer displayOrder,
            String status
    ) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call CLASS_SUBJECT_PKG.UPDATE_CLASS_SUBJECT(?, ?, ?, ?, ?, ?, ?, ?)}"
            );

            cs.setLong(1, classSubjectId);
            cs.setLong(2, academicYearClassId);
            cs.setLong(3, subjectId);
            cs.setString(4, isMandatory);
            cs.setBigDecimal(5, fullMarks);
            cs.setBigDecimal(6, passMarks);

            if (displayOrder == null) {
                cs.setNull(7, Types.INTEGER);
            } else {
                cs.setInt(7, displayOrder);
            }

            cs.setString(8, status);

            return cs;
        };

        CallableStatementCallback<Boolean> action = CallableStatement::execute;

        jdbcTemplate.execute(csc, action);
    }

    @Override
    public Optional<ClassSubjectResponse> findClassSubjectById(Long classSubjectId) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call CLASS_SUBJECT_PKG.GET_CLASS_SUBJECT_BY_ID(?, ?)}"
            );

            cs.setLong(1, classSubjectId);
            cs.registerOutParameter(2, OracleTypes.CURSOR);

            return cs;
        };

        CallableStatementCallback<Optional<ClassSubjectResponse>> action = cs -> {
            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                ClassSubjectResponseRowMapper mapper =
                        new ClassSubjectResponseRowMapper();

                if (rs.next()) {
                    return Optional.of(mapper.mapRow(rs, 1));
                }

                return Optional.empty();
            }
        };

        return jdbcTemplate.execute(csc, action);
    }

    @Override
    public List<ClassSubjectResponse> findAllClassSubjects() {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call CLASS_SUBJECT_PKG.GET_ALL_CLASS_SUBJECTS(?)}"
            );

            cs.registerOutParameter(1, OracleTypes.CURSOR);

            return cs;
        };

        CallableStatementCallback<List<ClassSubjectResponse>> action = cs -> {
            cs.execute();

            List<ClassSubjectResponse> classSubjects = new ArrayList<>();

            try (ResultSet rs = (ResultSet) cs.getObject(1)) {
                ClassSubjectResponseRowMapper mapper =
                        new ClassSubjectResponseRowMapper();

                int rowNum = 0;
                while (rs.next()) {
                    classSubjects.add(mapper.mapRow(rs, rowNum++));
                }
            }

            return classSubjects;
        };

        return jdbcTemplate.execute(csc, action);
    }

    @Override
    public List<ClassSubjectResponse> findSubjectsByAcademicClass(Long academicYearClassId) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call CLASS_SUBJECT_PKG.GET_SUBJECTS_BY_ACADEMIC_CLASS(?, ?)}"
            );

            cs.setLong(1, academicYearClassId);
            cs.registerOutParameter(2, OracleTypes.CURSOR);

            return cs;
        };

        CallableStatementCallback<List<ClassSubjectResponse>> action = cs -> {
            cs.execute();

            List<ClassSubjectResponse> subjects = new ArrayList<>();

            try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                ClassSubjectResponseRowMapper mapper =
                        new ClassSubjectResponseRowMapper();

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
    public void deleteClassSubject(Long classSubjectId) {
        CallableStatementCreator csc = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call CLASS_SUBJECT_PKG.DELETE_CLASS_SUBJECT(?)}"
            );

            cs.setLong(1, classSubjectId);

            return cs;
        };

        CallableStatementCallback<Boolean> action = CallableStatement::execute;

        jdbcTemplate.execute(csc, action);
    }
}