package com.jakirbd.student_management.examination.mapper;

import com.jakirbd.student_management.examination.dto.response.GradeScaleResponse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class GradeScaleRowMapper
        implements RowMapper<GradeScaleResponse> {

    @Override
    public GradeScaleResponse mapRow(
            ResultSet resultSet,
            int rowNum
    ) throws SQLException {

        GradeScaleResponse response = new GradeScaleResponse();

        response.setGradeScaleId(
                resultSet.getObject("GRADE_SCALE_ID", Long.class)
        );

        response.setGradeName(
                resultSet.getString("GRADE_NAME")
        );

        response.setLetterGrade(
                resultSet.getString("LETTER_GRADE")
        );

        response.setMinPercentage(
                resultSet.getBigDecimal("MIN_PERCENTAGE")
        );

        response.setMaxPercentage(
                resultSet.getBigDecimal("MAX_PERCENTAGE")
        );

        response.setGradePoint(
                resultSet.getBigDecimal("GRADE_POINT")
        );

        response.setDescription(
                resultSet.getString("DESCRIPTION")
        );

        response.setDisplayOrder(
                resultSet.getObject("DISPLAY_ORDER", Integer.class)
        );

        response.setStatus(
                resultSet.getString("STATUS")
        );

        Timestamp createdAt =
                resultSet.getTimestamp("CREATED_AT");

        if (createdAt != null) {
            response.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt =
                resultSet.getTimestamp("UPDATED_AT");

        if (updatedAt != null) {
            response.setUpdatedAt(updatedAt.toLocalDateTime());
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