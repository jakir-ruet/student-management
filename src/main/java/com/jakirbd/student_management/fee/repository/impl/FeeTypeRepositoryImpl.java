package com.jakirbd.student_management.fee.repository.impl;

import com.jakirbd.student_management.fee.dto.response.FeeTypeResponse;
import com.jakirbd.student_management.fee.mapper.FeeTypeRowMapper;
import com.jakirbd.student_management.fee.repository.FeeTypeRepository;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class FeeTypeRepositoryImpl
        implements FeeTypeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final FeeTypeRowMapper feeTypeRowMapper;

    public FeeTypeRepositoryImpl(
            JdbcTemplate jdbcTemplate,
            FeeTypeRowMapper feeTypeRowMapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.feeTypeRowMapper = feeTypeRowMapper;
    }

    @Override
    public Long createFeeType(
            String feeTypeName,
            String feeTypeCode,
            String description,
            Integer displayOrder,
            String status,
            Long createdBy
    ) {
        CallableStatementCreator creator = connection -> {
            CallableStatement statement = connection.prepareCall(
                    "{call FEE_TYPE_PKG.CREATE_FEE_TYPE(" +
                            "?, ?, ?, ?, ?, ?, ?)}"
            );

            statement.setString(1, feeTypeName);
            statement.setString(2, feeTypeCode);
            statement.setString(3, description);
            statement.setObject(
                    4,
                    displayOrder,
                    Types.NUMERIC
            );
            statement.setString(5, status);
            statement.setObject(
                    6,
                    createdBy,
                    Types.NUMERIC
            );
            statement.registerOutParameter(
                    7,
                    Types.NUMERIC
            );

            return statement;
        };

        CallableStatementCallback<Long> callback = statement -> {
            statement.execute();
            return statement.getObject(7, Long.class);
        };

        return jdbcTemplate.execute(creator, callback);
    }

    @Override
    public void updateFeeType(
            Long feeTypeId,
            String feeTypeName,
            String feeTypeCode,
            String description,
            Integer displayOrder,
            String status,
            Long updatedBy
    ) {
        executeUpdate(
                "{call FEE_TYPE_PKG.UPDATE_FEE_TYPE(" +
                        "?, ?, ?, ?, ?, ?, ?)}",
                statement -> {
                    statement.setObject(
                            1,
                            feeTypeId,
                            Types.NUMERIC
                    );
                    statement.setString(2, feeTypeName);
                    statement.setString(3, feeTypeCode);
                    statement.setString(4, description);
                    statement.setObject(
                            5,
                            displayOrder,
                            Types.NUMERIC
                    );
                    statement.setString(6, status);
                    statement.setObject(
                            7,
                            updatedBy,
                            Types.NUMERIC
                    );
                }
        );
    }

    @Override
    public Optional<FeeTypeResponse> findFeeTypeById(
            Long feeTypeId
    ) {
        List<FeeTypeResponse> results =
                executeCursorQuery(
                        "{call FEE_TYPE_PKG." +
                                "GET_FEE_TYPE_BY_ID(?, ?)}",
                        statement -> statement.setObject(
                                1,
                                feeTypeId,
                                Types.NUMERIC
                        ),
                        2
                );

        return results.stream().findFirst();
    }

    @Override
    public List<FeeTypeResponse> findAllFeeTypes() {
        return executeCursorQuery(
                "{call FEE_TYPE_PKG.GET_ALL_FEE_TYPES(?)}",
                statement -> {
                },
                1
        );
    }

    @Override
    public List<FeeTypeResponse> findActiveFeeTypes() {
        return executeCursorQuery(
                "{call FEE_TYPE_PKG." +
                        "GET_ACTIVE_FEE_TYPES(?)}",
                statement -> {
                },
                1
        );
    }

    @Override
    public void updateFeeTypeStatus(
            Long feeTypeId,
            String status,
            Long updatedBy
    ) {
        executeUpdate(
                "{call FEE_TYPE_PKG." +
                        "UPDATE_FEE_TYPE_STATUS(?, ?, ?)}",
                statement -> {
                    statement.setObject(
                            1,
                            feeTypeId,
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
    public void deleteFeeType(Long feeTypeId) {
        executeUpdate(
                "{call FEE_TYPE_PKG.DELETE_FEE_TYPE(?)}",
                statement -> statement.setObject(
                        1,
                        feeTypeId,
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

    private List<FeeTypeResponse> executeCursorQuery(
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

        CallableStatementCallback<List<FeeTypeResponse>> callback =
                statement -> {
                    statement.execute();

                    List<FeeTypeResponse> results =
                            new ArrayList<>();

                    try (ResultSet resultSet =
                                 (ResultSet) statement.getObject(
                                         cursorIndex
                                 )) {

                        int rowNum = 0;

                        while (resultSet.next()) {
                            results.add(
                                    feeTypeRowMapper.mapRow(
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