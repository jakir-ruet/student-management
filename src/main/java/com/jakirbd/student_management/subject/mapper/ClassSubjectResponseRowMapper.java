package com.jakirbd.student_management.subject.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.jakirbd.student_management.subject.dto.response.ClassSubjectResponse;

public class ClassSubjectResponseRowMapper
        implements RowMapper<ClassSubjectResponse> {

    @Override
    public ClassSubjectResponse mapRow(
            ResultSet rs,
            int rowNum
    ) throws SQLException {

        ClassSubjectResponse response =
                new ClassSubjectResponse();

        response.setClassSubjectId(
                rs.getLong("CLASS_SUBJECT_ID")
        );

        response.setAcademicYearClassId(
                rs.getLong("ACADEMIC_YEAR_CLASS_ID")
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

        response.setSubjectType(
                rs.getString("SUBJECT_TYPE")
        );

        response.setIsMandatory(
                rs.getString("IS_MANDATORY")
        );

        response.setFullMarks(
                rs.getBigDecimal("FULL_MARKS")
        );

        response.setPassMarks(
                rs.getBigDecimal("PASS_MARKS")
        );

        int displayOrder =
                rs.getInt("DISPLAY_ORDER");

        response.setDisplayOrder(
                rs.wasNull() ? null : displayOrder
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
