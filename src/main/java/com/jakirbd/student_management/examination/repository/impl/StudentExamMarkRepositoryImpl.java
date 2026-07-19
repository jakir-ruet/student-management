package com.jakirbd.student_management.examination.repository.impl;

import com.jakirbd.student_management.examination.dto.response.StudentExamMarkResponse;
import com.jakirbd.student_management.examination.dto.response.StudentExamResultResponse;
import com.jakirbd.student_management.examination.mapper.StudentExamMarkRowMapper;
import com.jakirbd.student_management.examination.mapper.StudentExamResultRowMapper;
import com.jakirbd.student_management.examination.repository.StudentExamMarkRepository;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class StudentExamMarkRepositoryImpl
        implements StudentExamMarkRepository {

    private final JdbcTemplate jdbcTemplate;
    private final StudentExamMarkRowMapper studentExamMarkRowMapper;
    private final StudentExamResultRowMapper studentExamResultRowMapper;

    public StudentExamMarkRepositoryImpl(
            JdbcTemplate jdbcTemplate,
            StudentExamMarkRowMapper studentExamMarkRowMapper,
            StudentExamResultRowMapper studentExamResultRowMapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.studentExamMarkRowMapper = studentExamMarkRowMapper;
        this.studentExamResultRowMapper = studentExamResultRowMapper;
    }

    @Override
    public Long createStudentMark(
            Long examSubjectId,
            Long studentId,
            BigDecimal obtainedMarks,
            String attendanceStatus,
            String remarks,
            Long createdBy
    ) {
        CallableStatementCreator creator = connection -> {
            CallableStatement statement = connection.prepareCall(
                    "{call STUDENT_EXAM_MARK_PKG." +
                            "CREATE_STUDENT_MARK(" +
                            "?, ?, ?, ?, ?, ?, ?)}"
            );

            statement.setObject(
                    1,
                    examSubjectId,
                    Types.NUMERIC
            );
            statement.setObject(
                    2,
                    studentId,
                    Types.NUMERIC
            );
            statement.setBigDecimal(3, obtainedMarks);
            statement.setString(4, attendanceStatus);
            statement.setString(5, remarks);
            statement.setObject(6, createdBy, Types.NUMERIC);
            statement.registerOutParameter(7, Types.NUMERIC);

            return statement;
        };

        CallableStatementCallback<Long> callback = statement -> {
            statement.execute();
            return statement.getObject(7, Long.class);
        };

        return jdbcTemplate.execute(creator, callback);
    }

    @Override
    public void updateStudentMark(
            Long studentExamMarkId,
            BigDecimal obtainedMarks,
            String attendanceStatus,
            String remarks,
            Long updatedBy
    ) {
        executeUpdate(
                "{call STUDENT_EXAM_MARK_PKG." +
                        "UPDATE_STUDENT_MARK(?, ?, ?, ?, ?)}",
                statement -> {
                    statement.setObject(
                            1,
                            studentExamMarkId,
                            Types.NUMERIC
                    );
                    statement.setBigDecimal(
                            2,
                            obtainedMarks
                    );
                    statement.setString(
                            3,
                            attendanceStatus
                    );
                    statement.setString(4, remarks);
                    statement.setObject(
                            5,
                            updatedBy,
                            Types.NUMERIC
                    );
                }
        );
    }

    @Override
    public Optional<StudentExamMarkResponse> findStudentMarkById(
            Long studentExamMarkId
    ) {
        List<StudentExamMarkResponse> results =
                executeCursorQuery(
                        "{call STUDENT_EXAM_MARK_PKG." +
                                "GET_STUDENT_MARK_BY_ID(?, ?)}",
                        statement -> statement.setObject(
                                1,
                                studentExamMarkId,
                                Types.NUMERIC
                        ),
                        2,
                        studentExamMarkRowMapper
                );

        return results.stream().findFirst();
    }

    @Override
    public List<StudentExamMarkResponse> findAllStudentMarks() {
        return executeCursorQuery(
                "{call STUDENT_EXAM_MARK_PKG." +
                        "GET_ALL_STUDENT_MARKS(?)}",
                statement -> {
                },
                1,
                studentExamMarkRowMapper
        );
    }

    @Override
    public List<StudentExamMarkResponse> findMarksByExamSubject(
            Long examSubjectId
    ) {
        return executeCursorQuery(
                "{call STUDENT_EXAM_MARK_PKG." +
                        "GET_MARKS_BY_EXAM_SUBJECT(?, ?)}",
                statement -> statement.setObject(
                        1,
                        examSubjectId,
                        Types.NUMERIC
                ),
                2,
                studentExamMarkRowMapper
        );
    }

    @Override
    public List<StudentExamMarkResponse> findMarksByStudent(
            Long studentId
    ) {
        return executeCursorQuery(
                "{call STUDENT_EXAM_MARK_PKG." +
                        "GET_MARKS_BY_STUDENT(?, ?)}",
                statement -> statement.setObject(
                        1,
                        studentId,
                        Types.NUMERIC
                ),
                2,
                studentExamMarkRowMapper
        );
    }

    @Override
    public List<StudentExamResultResponse> findStudentExamResults(
            Long studentId,
            Long examId
    ) {
        return executeCursorQuery(
                "{call STUDENT_EXAM_MARK_PKG." +
                        "GET_STUDENT_EXAM_RESULTS(?, ?, ?)}",
                statement -> {
                    statement.setObject(
                            1,
                            studentId,
                            Types.NUMERIC
                    );
                    statement.setObject(
                            2,
                            examId,
                            Types.NUMERIC
                    );
                },
                3,
                studentExamResultRowMapper
        );
    }

    @Override
    public void publishStudentMark(
            Long studentExamMarkId,
            Long updatedBy
    ) {
        executeUpdate(
                "{call STUDENT_EXAM_MARK_PKG." +
                        "PUBLISH_STUDENT_MARK(?, ?)}",
                statement -> {
                    statement.setObject(
                            1,
                            studentExamMarkId,
                            Types.NUMERIC
                    );
                    statement.setObject(
                            2,
                            updatedBy,
                            Types.NUMERIC
                    );
                }
        );
    }

    @Override
    public void unpublishStudentMark(
            Long studentExamMarkId,
            Long updatedBy
    ) {
        executeUpdate(
                "{call STUDENT_EXAM_MARK_PKG." +
                        "UNPUBLISH_STUDENT_MARK(?, ?)}",
                statement -> {
                    statement.setObject(
                            1,
                            studentExamMarkId,
                            Types.NUMERIC
                    );
                    statement.setObject(
                            2,
                            updatedBy,
                            Types.NUMERIC
                    );
                }
        );
    }

    @Override
    public void publishExamSubjectMarks(
            Long examSubjectId,
            Long updatedBy
    ) {
        executeUpdate(
                "{call STUDENT_EXAM_MARK_PKG." +
                        "PUBLISH_EXAM_SUBJECT_MARKS(?, ?)}",
                statement -> {
                    statement.setObject(
                            1,
                            examSubjectId,
                            Types.NUMERIC
                    );
                    statement.setObject(
                            2,
                            updatedBy,
                            Types.NUMERIC
                    );
                }
        );
    }

    @Override
    public void deleteStudentMark(Long studentExamMarkId) {
        executeUpdate(
                "{call STUDENT_EXAM_MARK_PKG." +
                        "DELETE_STUDENT_MARK(?)}",
                statement -> statement.setObject(
                        1,
                        studentExamMarkId,
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

    private <T> List<T> executeCursorQuery(
            String sql,
            StatementParameterSetter parameterSetter,
            int cursorIndex,
            RowMapper<T> rowMapper
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

        CallableStatementCallback<List<T>> callback =
                statement -> {
                    statement.execute();

                    List<T> results = new ArrayList<>();

                    try (ResultSet resultSet =
                                 (ResultSet) statement.getObject(
                                         cursorIndex
                                 )) {

                        int rowNum = 0;

                        while (resultSet.next()) {
                            results.add(
                                    rowMapper.mapRow(
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

    @FunctionalInterface
    private interface StatementParameterSetter {

        void setParameters(CallableStatement statement)
                throws SQLException;
    }
}