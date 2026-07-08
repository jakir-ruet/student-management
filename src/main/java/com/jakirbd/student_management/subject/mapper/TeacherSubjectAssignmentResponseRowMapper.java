package com.jakirbd.student_management.subject.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.jakirbd.student_management.subject.dto.response.TeacherSubjectAssignmentResponse;

public class TeacherSubjectAssignmentResponseRowMapper
        implements RowMapper<TeacherSubjectAssignmentResponse> {

    @Override
    public TeacherSubjectAssignmentResponse mapRow(
            ResultSet rs,
            int rowNum
    ) throws SQLException {

        TeacherSubjectAssignmentResponse response =
                new TeacherSubjectAssignmentResponse();

        response.setTeacherSubjectAssignmentId(
                rs.getLong("TEACHER_SUBJECT_ASSIGNMENT_ID")
        );

        response.setClassSubjectId(
                rs.getLong("CLASS_SUBJECT_ID")
        );

        response.setSectionShiftId(
                rs.getLong("SECTION_SHIFT_ID")
        );

        response.setTeacherId(
                rs.getLong("TEACHER_ID")
        );

        response.setSubjectId(
                rs.getLong("SUBJECT_ID")
        );

        response.setSubjectName(
                rs.getString("SUBJECT_NAME")
        );

        response.setSubjectCode(
                rs.getString("SUBJECT_CODE")
        );

        response.setAssignmentType(
                rs.getString("ASSIGNMENT_TYPE")
        );

        response.setStatus(
                rs.getString("STATUS")
        );

        Timestamp createdAt =
                rs.getTimestamp("CREATED_AT");

        response.setCreatedAt(
                createdAt != null
                        ? createdAt.toLocalDateTime()
                        : null
        );

        Timestamp updatedAt =
                rs.getTimestamp("UPDATED_AT");

        response.setUpdatedAt(
                updatedAt != null
                        ? updatedAt.toLocalDateTime()
                        : null
        );

        return response;
    }
}
