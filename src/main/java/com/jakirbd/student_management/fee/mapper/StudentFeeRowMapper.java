package com.jakirbd.student_management.fee.mapper;

import com.jakirbd.student_management.fee.dto.response.StudentFeeResponse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class StudentFeeRowMapper
        implements RowMapper<StudentFeeResponse> {

    @Override
    public StudentFeeResponse mapRow(
            ResultSet resultSet,
            int rowNum
    ) throws SQLException {

        StudentFeeResponse response =
                new StudentFeeResponse();

        response.setStudentFeeId(
                resultSet.getObject(
                        "STUDENT_FEE_ID",
                        Long.class
                )
        );

        response.setFeeStructureId(
                resultSet.getObject(
                        "FEE_STRUCTURE_ID",
                        Long.class
                )
        );

        response.setStudentId(
                resultSet.getObject(
                        "STUDENT_ID",
                        Long.class
                )
        );

        response.setBillingPeriod(
                resultSet.getString("BILLING_PERIOD")
        );

        Date dueDate = resultSet.getDate("DUE_DATE");

        if (dueDate != null) {
            response.setDueDate(dueDate.toLocalDate());
        }

        response.setBaseAmount(
                resultSet.getBigDecimal("BASE_AMOUNT")
        );

        response.setDiscountAmount(
                resultSet.getBigDecimal("DISCOUNT_AMOUNT")
        );

        response.setFineAmount(
                resultSet.getBigDecimal("FINE_AMOUNT")
        );

        response.setTotalAmount(
                resultSet.getBigDecimal("TOTAL_AMOUNT")
        );

        response.setPaidAmount(
                resultSet.getBigDecimal("PAID_AMOUNT")
        );

        response.setBalanceAmount(
                resultSet.getBigDecimal("BALANCE_AMOUNT")
        );

        response.setStatus(
                resultSet.getString("STATUS")
        );

        response.setRemarks(
                resultSet.getString("REMARKS")
        );

        Timestamp createdAt =
                resultSet.getTimestamp("CREATED_AT");

        if (createdAt != null) {
            response.setCreatedAt(
                    createdAt.toLocalDateTime()
            );
        }

        Timestamp updatedAt =
                resultSet.getTimestamp("UPDATED_AT");

        if (updatedAt != null) {
            response.setUpdatedAt(
                    updatedAt.toLocalDateTime()
            );
        }

        response.setCreatedBy(
                resultSet.getObject("CREATED_BY", Long.class)
        );

        response.setUpdatedBy(
                resultSet.getObject("UPDATED_BY", Long.class)
        );

        return response;
    }
}