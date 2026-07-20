package com.jakirbd.student_management.fee.mapper;

import com.jakirbd.student_management.fee.dto.response.FeeTypeResponse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class FeeTypeRowMapper
        implements RowMapper<FeeTypeResponse> {

    @Override
    public FeeTypeResponse mapRow(
            ResultSet resultSet,
            int rowNum
    ) throws SQLException {

        FeeTypeResponse response = new FeeTypeResponse();

        response.setFeeTypeId(
                resultSet.getObject("FEE_TYPE_ID", Long.class)
        );

        response.setFeeTypeName(
                resultSet.getString("FEE_TYPE_NAME")
        );

        response.setFeeTypeCode(
                resultSet.getString("FEE_TYPE_CODE")
        );

        response.setDescription(
                resultSet.getString("DESCRIPTION")
        );

        response.setDisplayOrder(
                resultSet.getObject(
                        "DISPLAY_ORDER",
                        Integer.class
                )
        );

        response.setStatus(
                resultSet.getString("STATUS")
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