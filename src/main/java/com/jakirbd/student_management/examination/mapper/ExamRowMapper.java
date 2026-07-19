package com.jakirbd.student_management.examination.mapper;

import com.jakirbd.student_management.examination.dto.response.ExamResponse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ExamRowMapper implements RowMapper<ExamResponse> {

    @Override
    public ExamResponse mapRow(ResultSet resultSet, int rowNum)
            throws SQLException {

        ExamResponse response = new ExamResponse();

        response.setExamId(
                resultSet.getObject("EXAM_ID", Long.class)
        );

        response.setAcademicYearId(
                resultSet.getObject("ACADEMIC_YEAR_ID", Long.class)
        );

        response.setAcademicYearName(
                resultSet.getString("ACADEMIC_YEAR_NAME")
        );

        response.setExamName(
                resultSet.getString("EXAM_NAME")
        );

        response.setExamCode(
                resultSet.getString("EXAM_CODE")
        );

        response.setExamType(
                resultSet.getString("EXAM_TYPE")
        );

        if (resultSet.getDate("START_DATE") != null) {
            response.setStartDate(
                    resultSet.getDate("START_DATE").toLocalDate()
            );
        }

        if (resultSet.getDate("END_DATE") != null) {
            response.setEndDate(
                    resultSet.getDate("END_DATE").toLocalDate()
            );
        }

        response.setDescription(
                resultSet.getString("DESCRIPTION")
        );

        response.setStatus(
                resultSet.getString("STATUS")
        );

        if (resultSet.getTimestamp("CREATED_AT") != null) {
            response.setCreatedAt(
                    resultSet.getTimestamp("CREATED_AT")
                            .toLocalDateTime()
            );
        }

        if (resultSet.getTimestamp("UPDATED_AT") != null) {
            response.setUpdatedAt(
                    resultSet.getTimestamp("UPDATED_AT")
                            .toLocalDateTime()
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