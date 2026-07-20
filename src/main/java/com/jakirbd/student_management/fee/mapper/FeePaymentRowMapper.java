package com.jakirbd.student_management.fee.mapper;

import com.jakirbd.student_management.fee.dto.response.FeePaymentResponse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class FeePaymentRowMapper
        implements RowMapper<FeePaymentResponse> {

    @Override
    public FeePaymentResponse mapRow(
            ResultSet resultSet,
            int rowNum
    ) throws SQLException {

        FeePaymentResponse response =
                new FeePaymentResponse();

        response.setFeePaymentId(
                resultSet.getObject(
                        "FEE_PAYMENT_ID",
                        Long.class
                )
        );

        response.setStudentId(
                resultSet.getObject(
                        "STUDENT_ID",
                        Long.class
                )
        );

        response.setReceiptNumber(
                resultSet.getString("RECEIPT_NUMBER")
        );

        Timestamp paymentDate =
                resultSet.getTimestamp("PAYMENT_DATE");

        if (paymentDate != null) {
            response.setPaymentDate(
                    paymentDate.toLocalDateTime()
            );
        }

        response.setPaymentAmount(
                resultSet.getBigDecimal("PAYMENT_AMOUNT")
        );

        response.setPaymentMethod(
                resultSet.getString("PAYMENT_METHOD")
        );

        response.setTransactionReference(
                resultSet.getString(
                        "TRANSACTION_REFERENCE"
                )
        );

        response.setPaymentStatus(
                resultSet.getString("PAYMENT_STATUS")
        );

        response.setNotes(
                resultSet.getString("NOTES")
        );

        response.setReceivedBy(
                resultSet.getObject(
                        "RECEIVED_BY",
                        Long.class
                )
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