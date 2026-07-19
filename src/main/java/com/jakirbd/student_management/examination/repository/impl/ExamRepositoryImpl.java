package com.jakirbd.student_management.examination.repository.impl;

import com.jakirbd.student_management.examination.dto.response.ExamResponse;
import com.jakirbd.student_management.examination.mapper.ExamRowMapper;
import com.jakirbd.student_management.examination.repository.ExamRepository;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ExamRepositoryImpl implements ExamRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ExamRowMapper examRowMapper;

    public ExamRepositoryImpl(
            JdbcTemplate jdbcTemplate,
            ExamRowMapper examRowMapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.examRowMapper = examRowMapper;
    }

    @Override
    public Long createExam(
            Long academicYearId,
            String examName,
            String examCode,
            String examType,
            LocalDate startDate,
            LocalDate endDate,
            String description,
            String status,
            Long createdBy
    ) {
        CallableStatementCreator creator = connection -> {
            CallableStatement statement = connection.prepareCall(
                    "{call EXAM_PKG.CREATE_EXAM(" +
                            "?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}"
            );

            statement.setObject(1, academicYearId, Types.NUMERIC);
            statement.setString(2, examName);
            statement.setString(3, examCode);
            statement.setString(4, examType);
            statement.setDate(5, toSqlDate(startDate));
            statement.setDate(6, toSqlDate(endDate));
            statement.setString(7, description);
            statement.setString(8, status);
            statement.setObject(9, createdBy, Types.NUMERIC);
            statement.registerOutParameter(10, Types.NUMERIC);

            return statement;
        };

        CallableStatementCallback<Long> callback = statement -> {
            statement.execute();
            return statement.getObject(10, Long.class);
        };

        return jdbcTemplate.execute(creator, callback);
    }

    @Override
    public void updateExam(
            Long examId,
            Long academicYearId,
            String examName,
            String examCode,
            String examType,
            LocalDate startDate,
            LocalDate endDate,
            String description,
            String status,
            Long updatedBy
    ) {
        executeUpdate(
                "{call EXAM_PKG.UPDATE_EXAM(" +
                        "?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}",
                statement -> {
                    statement.setObject(1, examId, Types.NUMERIC);
                    statement.setObject(
                            2,
                            academicYearId,
                            Types.NUMERIC
                    );
                    statement.setString(3, examName);
                    statement.setString(4, examCode);
                    statement.setString(5, examType);
                    statement.setDate(6, toSqlDate(startDate));
                    statement.setDate(7, toSqlDate(endDate));
                    statement.setString(8, description);
                    statement.setString(9, status);
                    statement.setObject(
                            10,
                            updatedBy,
                            Types.NUMERIC
                    );
                }
        );
    }

    @Override
    public Optional<ExamResponse> findExamById(Long examId) {
        List<ExamResponse> results = executeCursorQuery(
                "{call EXAM_PKG.GET_EXAM_BY_ID(?, ?)}",
                statement -> statement.setObject(
                        1,
                        examId,
                        Types.NUMERIC
                ),
                2
        );

        return results.stream().findFirst();
    }

    @Override
    public List<ExamResponse> findAllExams() {
        return executeCursorQuery(
                "{call EXAM_PKG.GET_ALL_EXAMS(?)}",
                statement -> {
                },
                1
        );
    }

    @Override
    public List<ExamResponse> findExamsByAcademicYear(
            Long academicYearId
    ) {
        return executeCursorQuery(
                "{call EXAM_PKG.GET_EXAMS_BY_ACADEMIC_YEAR(?, ?)}",
                statement -> statement.setObject(
                        1,
                        academicYearId,
                        Types.NUMERIC
                ),
                2
        );
    }

    @Override
    public List<ExamResponse> findExamsByStatus(String status) {
        return executeCursorQuery(
                "{call EXAM_PKG.GET_EXAMS_BY_STATUS(?, ?)}",
                statement -> statement.setString(1, status),
                2
        );
    }

    @Override
    public void updateExamStatus(
            Long examId,
            String status,
            Long updatedBy
    ) {
        executeUpdate(
                "{call EXAM_PKG.UPDATE_EXAM_STATUS(?, ?, ?)}",
                statement -> {
                    statement.setObject(
                            1,
                            examId,
                            Types.NUMERIC
                    );
                    statement.setString(2, status);
                    statement.setObject(
                            3,
                            updatedBy,
                            Types.NUMERIC
                    );
                }
        );
    }

    @Override
    public void deleteExam(Long examId) {
        executeUpdate(
                "{call EXAM_PKG.DELETE_EXAM(?)}",
                statement -> statement.setObject(
                        1,
                        examId,
                        Types.NUMERIC
                )
        );
    }

    private void executeUpdate(
            String sql,
            StatementParameterSetter parameterSetter
    ) {
        CallableStatementCreator creator = connection -> {
            CallableStatement statement =
                    connection.prepareCall(sql);

            parameterSetter.setParameters(statement);

            return statement;
        };

        CallableStatementCallback<Void> callback = statement -> {
            statement.execute();
            return null;
        };

        jdbcTemplate.execute(creator, callback);
    }

    private List<ExamResponse> executeCursorQuery(
            String sql,
            StatementParameterSetter parameterSetter,
            int cursorIndex
    ) {
        CallableStatementCreator creator = connection -> {
            CallableStatement statement =
                    connection.prepareCall(sql);

            parameterSetter.setParameters(statement);

            statement.registerOutParameter(
                    cursorIndex,
                    Types.REF_CURSOR
            );

            return statement;
        };

        CallableStatementCallback<List<ExamResponse>> callback =
                statement -> {
                    statement.execute();

                    List<ExamResponse> results = new ArrayList<>();

                    try (ResultSet resultSet =
                                 (ResultSet) statement.getObject(
                                         cursorIndex
                                 )) {

                        int rowNum = 0;

                        while (resultSet.next()) {
                            results.add(
                                    examRowMapper.mapRow(
                                            resultSet,
                                            rowNum++
                                    )
                            );
                        }
                    }

                    return results;
                };

        return jdbcTemplate.execute(creator, callback);
    }

    private Date toSqlDate(LocalDate date) {
        return date == null ? null : Date.valueOf(date);
    }

    @FunctionalInterface
    private interface StatementParameterSetter {

        void setParameters(CallableStatement statement)
                throws SQLException;
    }
}