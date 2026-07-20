package com.jakirbd.student_management.fee.mapper;

import com.jakirbd.student_management.fee.dto.response.StudentFeeDiscountResponse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class StudentFeeDiscountRowMapper
        implements RowMapper<StudentFeeDiscountResponse> {

    @Override
    public StudentFeeDiscountResponse mapRow(
            ResultSet resultSet,
            int rowNum
    ) throws SQLException {

        StudentFeeDiscountResponse response =
                new StudentFeeDiscountResponse();

        response.setStudentFeeDiscountId(
                resultSet.getObject(
                        "STUDENT_FEE_DISCOUNT_ID",
                        Long.class
                )
        );

        response.setStudentFeeId(
                resultSet.getObject(
                        "STUDENT_FEE_ID",
                        Long.class
                )
        );

        response.setDiscountType(
                resultSet.getString("DISCOUNT_TYPE")
        );

        response.setDiscountValue(
                resultSet.getBigDecimal("DISCOUNT_VALUE")
        );

        response.setDiscountAmount(
                resultSet.getBigDecimal("DISCOUNT_AMOUNT")
        );

        response.setReason(
                resultSet.getString("REASON")
        );

        response.setApprovalStatus(
                resultSet.getString("APPROVAL_STATUS")
        );

        response.setApprovedBy(
                resultSet.getObject("APPROVED_BY", Long.class)
        );

        Timestamp approvedAt =
                resultSet.getTimestamp("APPROVED_AT");

        if (approvedAt != null) {
            response.setApprovedAt(
                    approvedAt.toLocalDateTime()
            );
        }

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