package com.jakirbd.student_management.examination.repository.impl;

import com.jakirbd.student_management.examination.dto.response.ExamSubjectResponse;
import com.jakirbd.student_management.examination.mapper.ExamSubjectRowMapper;
import com.jakirbd.student_management.examination.repository.ExamSubjectRepository;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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
public class ExamSubjectRepositoryImpl
        implements ExamSubjectRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ExamSubjectRowMapper examSubjectRowMapper;

    public ExamSubjectRepositoryImpl(
            JdbcTemplate jdbcTemplate,
            ExamSubjectRowMapper examSubjectRowMapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.examSubjectRowMapper = examSubjectRowMapper;
    }

    @Override
    public Long createExamSubject(
            Long examId,
            Long academicYearClassId,
            Long sectionId,
            Long subjectId,
            LocalDate examDate,
            String startTime,
            String endTime,
            Integer durationMinutes,
            BigDecimal fullMarks,
            BigDecimal passMarks,
            String roomNumber,
            String instructions,
            String status,
            Long createdBy
    ) {
        CallableStatementCreator creator = connection -> {
            CallableStatement statement = connection.prepareCall(
                    "{call EXAM_SUBJECT_PKG.CREATE_EXAM_SUBJECT(" +
                            "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}"
            );

            statement.setObject(1, examId, Types.NUMERIC);
            statement.setObject(
                    2,
                    academicYearClassId,
                    Types.NUMERIC
            );
            statement.setObject(3, sectionId, Types.NUMERIC);
            statement.setObject(4, subjectId, Types.NUMERIC);
            statement.setDate(5, toSqlDate(examDate));
            statement.setString(6, startTime);
            statement.setString(7, endTime);
            statement.setObject(
                    8,
                    durationMinutes,
                    Types.NUMERIC
            );
            statement.setBigDecimal(9, fullMarks);
            statement.setBigDecimal(10, passMarks);
            statement.setString(11, roomNumber);
            statement.setString(12, instructions);
            statement.setString(13, status);
            statement.setObject(14, createdBy, Types.NUMERIC);
            statement.registerOutParameter(15, Types.NUMERIC);

            return statement;
        };

        CallableStatementCallback<Long> callback = statement -> {
            statement.execute();
            return statement.getObject(15, Long.class);
        };

        return jdbcTemplate.execute(creator, callback);
    }

    @Override
    public void updateExamSubject(
            Long examSubjectId,
            Long examId,
            Long academicYearClassId,
            Long sectionId,
            Long subjectId,
            LocalDate examDate,
            String startTime,
            String endTime,
            Integer durationMinutes,
            BigDecimal fullMarks,
            BigDecimal passMarks,
            String roomNumber,
            String instructions,
            String status,
            Long updatedBy
    ) {
        executeUpdate(
                "{call EXAM_SUBJECT_PKG.UPDATE_EXAM_SUBJECT(" +
                        "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}",
                statement -> {
                    statement.setObject(
                            1,
                            examSubjectId,
                            Types.NUMERIC
                    );
                    statement.setObject(
                            2,
                            examId,
                            Types.NUMERIC
                    );
                    statement.setObject(
                            3,
                            academicYearClassId,
                            Types.NUMERIC
                    );
                    statement.setObject(
                            4,
                            sectionId,
                            Types.NUMERIC
                    );
                    statement.setObject(
                            5,
                            subjectId,
                            Types.NUMERIC
                    );
                    statement.setDate(
                            6,
                            toSqlDate(examDate)
                    );
                    statement.setString(7, startTime);
                    statement.setString(8, endTime);
                    statement.setObject(
                            9,
                            durationMinutes,
                            Types.NUMERIC
                    );
                    statement.setBigDecimal(10, fullMarks);
                    statement.setBigDecimal(11, passMarks);
                    statement.setString(12, roomNumber);
                    statement.setString(13, instructions);
                    statement.setString(14, status);
                    statement.setObject(
                            15,
                            updatedBy,
                            Types.NUMERIC
                    );
                }
        );
    }

    @Override
    public Optional<ExamSubjectResponse> findExamSubjectById(
            Long examSubjectId
    ) {
        List<ExamSubjectResponse> results =
                executeCursorQuery(
                        "{call EXAM_SUBJECT_PKG." +
                                "GET_EXAM_SUBJECT_BY_ID(?, ?)}",
                        statement -> statement.setObject(
                                1,
                                examSubjectId,
                                Types.NUMERIC
                        ),
                        2
                );

        return results.stream().findFirst();
    }

    @Override
    public List<ExamSubjectResponse> findAllExamSubjects() {
        return executeCursorQuery(
                "{call EXAM_SUBJECT_PKG." +
                        "GET_ALL_EXAM_SUBJECTS(?)}",
                statement -> {
                },
                1
        );
    }

    @Override
    public List<ExamSubjectResponse> findExamSubjectsByExam(
            Long examId
    ) {
        return executeCursorQuery(
                "{call EXAM_SUBJECT_PKG." +
                        "GET_SUBJECTS_BY_EXAM(?, ?)}",
                statement -> statement.setObject(
                        1,
                        examId,
                        Types.NUMERIC
                ),
                2
        );
    }

    @Override
    public List<ExamSubjectResponse> findExamSubjectsByClass(
            Long academicYearClassId
    ) {
        return executeCursorQuery(
                "{call EXAM_SUBJECT_PKG." +
                        "GET_SUBJECTS_BY_CLASS(?, ?)}",
                statement -> statement.setObject(
                        1,
                        academicYearClassId,
                        Types.NUMERIC
                ),
                2
        );
    }

    @Override
    public List<ExamSubjectResponse> findExamSubjectsBySection(
            Long sectionId
    ) {
        return executeCursorQuery(
                "{call EXAM_SUBJECT_PKG." +
                        "GET_SUBJECTS_BY_SECTION(?, ?)}",
                statement -> statement.setObject(
                        1,
                        sectionId,
                        Types.NUMERIC
                ),
                2
        );
    }

    @Override
    public void updateExamSubjectStatus(
            Long examSubjectId,
            String status,
            Long updatedBy
    ) {
        executeUpdate(
                "{call EXAM_SUBJECT_PKG." +
                        "UPDATE_EXAM_SUBJECT_STATUS(?, ?, ?)}",
                statement -> {
                    statement.setObject(
                            1,
                            examSubjectId,
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
    public void deleteExamSubject(Long examSubjectId) {
        executeUpdate(
                "{call EXAM_SUBJECT_PKG." +
                        "DELETE_EXAM_SUBJECT(?)}",
                statement -> statement.setObject(
                        1,
                        examSubjectId,
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

    private List<ExamSubjectResponse> executeCursorQuery(
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

        CallableStatementCallback<List<ExamSubjectResponse>> callback =
                statement -> {
                    statement.execute();

                    List<ExamSubjectResponse> results =
                            new ArrayList<>();

                    try (ResultSet resultSet =
                                 (ResultSet) statement.getObject(
                                         cursorIndex
                                 )) {

                        int rowNum = 0;

                        while (resultSet.next()) {
                            results.add(
                                    examSubjectRowMapper.mapRow(
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