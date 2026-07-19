package com.jakirbd.student_management.examination.repository.impl;

import com.jakirbd.student_management.examination.dto.response.GradeCalculationResponse;
import com.jakirbd.student_management.examination.dto.response.GradeScaleResponse;
import com.jakirbd.student_management.examination.mapper.GradeScaleRowMapper;
import com.jakirbd.student_management.examination.repository.GradeScaleRepository;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
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
public class GradeScaleRepositoryImpl
        implements GradeScaleRepository {

    private final JdbcTemplate jdbcTemplate;
    private final GradeScaleRowMapper gradeScaleRowMapper;

    public GradeScaleRepositoryImpl(
            JdbcTemplate jdbcTemplate,
            GradeScaleRowMapper gradeScaleRowMapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.gradeScaleRowMapper = gradeScaleRowMapper;
    }

    @Override
    public Long createGradeScale(
            String gradeName,
            String letterGrade,
            BigDecimal minPercentage,
            BigDecimal maxPercentage,
            BigDecimal gradePoint,
            String description,
            Integer displayOrder,
            String status,
            Long createdBy
    ) {
        CallableStatementCreator creator = connection -> {
            CallableStatement statement = connection.prepareCall(
                    "{call GRADE_SCALE_PKG.CREATE_GRADE_SCALE(" +
                            "?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}"
            );

            statement.setString(1, gradeName);
            statement.setString(2, letterGrade);
            statement.setBigDecimal(3, minPercentage);
            statement.setBigDecimal(4, maxPercentage);
            statement.setBigDecimal(5, gradePoint);
            statement.setString(6, description);
            statement.setObject(
                    7,
                    displayOrder,
                    Types.NUMERIC
            );
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
    public void updateGradeScale(
            Long gradeScaleId,
            String gradeName,
            String letterGrade,
            BigDecimal minPercentage,
            BigDecimal maxPercentage,
            BigDecimal gradePoint,
            String description,
            Integer displayOrder,
            String status,
            Long updatedBy
    ) {
        executeUpdate(
                "{call GRADE_SCALE_PKG.UPDATE_GRADE_SCALE(" +
                        "?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}",
                statement -> {
                    statement.setObject(
                            1,
                            gradeScaleId,
                            Types.NUMERIC
                    );
                    statement.setString(2, gradeName);
                    statement.setString(3, letterGrade);
                    statement.setBigDecimal(
                            4,
                            minPercentage
                    );
                    statement.setBigDecimal(
                            5,
                            maxPercentage
                    );
                    statement.setBigDecimal(6, gradePoint);
                    statement.setString(7, description);
                    statement.setObject(
                            8,
                            displayOrder,
                            Types.NUMERIC
                    );
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
    public Optional<GradeScaleResponse> findGradeScaleById(
            Long gradeScaleId
    ) {
        List<GradeScaleResponse> results =
                executeCursorQuery(
                        "{call GRADE_SCALE_PKG." +
                                "GET_GRADE_SCALE_BY_ID(?, ?)}",
                        statement -> statement.setObject(
                                1,
                                gradeScaleId,
                                Types.NUMERIC
                        ),
                        2
                );

        return results.stream().findFirst();
    }

    @Override
    public List<GradeScaleResponse> findAllGradeScales() {
        return executeCursorQuery(
                "{call GRADE_SCALE_PKG." +
                        "GET_ALL_GRADE_SCALES(?)}",
                statement -> {
                },
                1
        );
    }

    @Override
    public List<GradeScaleResponse> findActiveGradeScales() {
        return executeCursorQuery(
                "{call GRADE_SCALE_PKG." +
                        "GET_ACTIVE_GRADE_SCALES(?)}",
                statement -> {
                },
                1
        );
    }

    @Override
    public GradeCalculationResponse calculateGrade(
            BigDecimal percentage
    ) {
        CallableStatementCreator creator = connection -> {
            CallableStatement statement = connection.prepareCall(
                    "{call GRADE_SCALE_PKG.CALCULATE_GRADE(" +
                            "?, ?, ?)}"
            );

            statement.setBigDecimal(1, percentage);
            statement.registerOutParameter(2, Types.VARCHAR);
            statement.registerOutParameter(3, Types.NUMERIC);

            return statement;
        };

        CallableStatementCallback<GradeCalculationResponse> callback =
                statement -> {
                    statement.execute();

                    return new GradeCalculationResponse(
                            percentage,
                            statement.getString(2),
                            statement.getBigDecimal(3)
                    );
                };

        return jdbcTemplate.execute(creator, callback);
    }

    @Override
    public void updateGradeScaleStatus(
            Long gradeScaleId,
            String status,
            Long updatedBy
    ) {
        executeUpdate(
                "{call GRADE_SCALE_PKG." +
                        "UPDATE_GRADE_SCALE_STATUS(?, ?, ?)}",
                statement -> {
                    statement.setObject(
                            1,
                            gradeScaleId,
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
    public void deleteGradeScale(Long gradeScaleId) {
        executeUpdate(
                "{call GRADE_SCALE_PKG." +
                        "DELETE_GRADE_SCALE(?)}",
                statement -> statement.setObject(
                        1,
                        gradeScaleId,
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

    private List<GradeScaleResponse> executeCursorQuery(
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

        CallableStatementCallback<List<GradeScaleResponse>> callback =
                statement -> {
                    statement.execute();

                    List<GradeScaleResponse> results =
                            new ArrayList<>();

                    try (ResultSet resultSet =
                                 (ResultSet) statement.getObject(
                                         cursorIndex
                                 )) {

                        int rowNum = 0;

                        while (resultSet.next()) {
                            results.add(
                                    gradeScaleRowMapper.mapRow(
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