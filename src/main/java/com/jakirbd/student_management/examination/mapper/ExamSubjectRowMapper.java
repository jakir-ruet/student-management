package com.jakirbd.student_management.examination.mapper;

import com.jakirbd.student_management.examination.dto.response.ExamSubjectResponse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class ExamSubjectRowMapper
        implements RowMapper<ExamSubjectResponse> {

    @Override
    public ExamSubjectResponse mapRow(
            ResultSet resultSet,
            int rowNum
    ) throws SQLException {

        ExamSubjectResponse response = new ExamSubjectResponse();

        response.setExamSubjectId(
                resultSet.getObject("EXAM_SUBJECT_ID", Long.class)
        );

        response.setExamId(
                resultSet.getObject("EXAM_ID", Long.class)
        );

        response.setAcademicYearClassId(
                resultSet.getObject(
                        "ACADEMIC_YEAR_CLASS_ID",
                        Long.class
                )
        );

        response.setSectionId(
                resultSet.getObject("SECTION_ID", Long.class)
        );

        response.setSubjectId(
                resultSet.getObject("SUBJECT_ID", Long.class)
        );

        Date examDate = resultSet.getDate("EXAM_DATE");

        if (examDate != null) {
            response.setExamDate(examDate.toLocalDate());
        }

        response.setStartTime(
                resultSet.getString("START_TIME")
        );

        response.setEndTime(
                resultSet.getString("END_TIME")
        );

        response.setDurationMinutes(
                resultSet.getObject(
                        "DURATION_MINUTES",
                        Integer.class
                )
        );

        response.setFullMarks(
                resultSet.getBigDecimal("FULL_MARKS")
        );

        response.setPassMarks(
                resultSet.getBigDecimal("PASS_MARKS")
        );

        response.setRoomNumber(
                resultSet.getString("ROOM_NUMBER")
        );

        response.setInstructions(
                resultSet.getString("INSTRUCTIONS")
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