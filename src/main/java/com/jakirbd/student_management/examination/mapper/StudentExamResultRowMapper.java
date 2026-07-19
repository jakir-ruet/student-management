package com.jakirbd.student_management.examination.mapper;

import com.jakirbd.student_management.examination.dto.response.StudentExamResultResponse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class StudentExamResultRowMapper
        implements RowMapper<StudentExamResultResponse> {

    @Override
    public StudentExamResultResponse mapRow(
            ResultSet resultSet,
            int rowNum
    ) throws SQLException {

        StudentExamResultResponse response =
                new StudentExamResultResponse();

        response.setStudentExamMarkId(
                resultSet.getObject(
                        "STUDENT_EXAM_MARK_ID",
                        Long.class
                )
        );

        response.setExamSubjectId(
                resultSet.getObject("EXAM_SUBJECT_ID", Long.class)
        );

        response.setExamId(
                resultSet.getObject("EXAM_ID", Long.class)
        );

        response.setSubjectId(
                resultSet.getObject("SUBJECT_ID", Long.class)
        );

        response.setFullMarks(
                resultSet.getBigDecimal("FULL_MARKS")
        );

        response.setPassMarks(
                resultSet.getBigDecimal("PASS_MARKS")
        );

        response.setStudentId(
                resultSet.getObject("STUDENT_ID", Long.class)
        );

        response.setObtainedMarks(
                resultSet.getBigDecimal("OBTAINED_MARKS")
        );

        response.setPercentage(
                resultSet.getBigDecimal("PERCENTAGE")
        );

        response.setLetterGrade(
                resultSet.getString("LETTER_GRADE")
        );

        response.setGradePoint(
                resultSet.getBigDecimal("GRADE_POINT")
        );

        response.setResultStatus(
                resultSet.getString("RESULT_STATUS")
        );

        response.setAttendanceStatus(
                resultSet.getString("ATTENDANCE_STATUS")
        );

        response.setRemarks(
                resultSet.getString("REMARKS")
        );

        response.setPublishedFlag(
                resultSet.getString("PUBLISHED_FLAG")
        );

        Timestamp markedAt =
                resultSet.getTimestamp("MARKED_AT");

        if (markedAt != null) {
            response.setMarkedAt(markedAt.toLocalDateTime());
        }

        Timestamp publishedAt =
                resultSet.getTimestamp("PUBLISHED_AT");

        if (publishedAt != null) {
            response.setPublishedAt(
                    publishedAt.toLocalDateTime()
            );
        }

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