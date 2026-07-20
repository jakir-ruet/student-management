package com.jakirbd.student_management.fee.mapper;

import com.jakirbd.student_management.fee.dto.response.FeePaymentAllocationResponse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class FeePaymentAllocationRowMapper
        implements RowMapper<FeePaymentAllocationResponse> {

    @Override
    public FeePaymentAllocationResponse mapRow(
            ResultSet resultSet,
            int rowNum
    ) throws SQLException {

        FeePaymentAllocationResponse response =
                new FeePaymentAllocationResponse();

        response.setPaymentAllocationId(
                resultSet.getObject(
                        "PAYMENT_ALLOCATION_ID",
                        Long.class
                )
        );

        response.setFeePaymentId(
                resultSet.getObject(
                        "FEE_PAYMENT_ID",
                        Long.class
                )
        );

        response.setStudentFeeId(
                resultSet.getObject(
                        "STUDENT_FEE_ID",
                        Long.class
                )
        );

        response.setAllocatedAmount(
                resultSet.getBigDecimal(
                        "ALLOCATED_AMOUNT"
                )
        );

        Timestamp createdAt =
                resultSet.getTimestamp("CREATED_AT");

        if (createdAt != null) {
            response.setCreatedAt(
                    createdAt.toLocalDateTime()
            );
        }

        response.setCreatedBy(
                resultSet.getObject(
                        "CREATED_BY",
                        Long.class
                )
        );

        return response;
    }
}