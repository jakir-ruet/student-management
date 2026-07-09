package com.jakirbd.student_management.subject.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.jakirbd.student_management.subject.model.ClassSubject;

public class ClassSubjectRowMapper
        implements RowMapper<ClassSubject> {

    @Override
    public ClassSubject mapRow(
            ResultSet rs,
            int rowNum
    ) throws SQLException {

        ClassSubject classSubject = new ClassSubject();

        classSubject.setClassSubjectId(
                rs.getLong("CLASS_SUBJECT_ID")
        );

        classSubject.setAcademicYearClassId(
                rs.getLong("ACADEMIC_YEAR_CLASS_ID")
        );

        classSubject.setSubjectId(
                rs.getLong("SUBJECT_ID")
        );

        classSubject.setIsMandatory(
                rs.getString("IS_MANDATORY")
        );

        classSubject.setFullMarks(
                rs.getBigDecimal("FULL_MARKS")
        );

        classSubject.setPassMarks(
                rs.getBigDecimal("PASS_MARKS")
        );

        int displayOrder = rs.getInt("DISPLAY_ORDER");

        classSubject.setDisplayOrder(
                rs.wasNull() ? null : displayOrder
        );

        classSubject.setStatus(
                rs.getString("STATUS")
        );

        Timestamp createdAt =
                rs.getTimestamp("CREATED_AT");

        classSubject.setCreatedAt(
                createdAt != null
                        ? createdAt.toLocalDateTime()
                        : null
        );

        Timestamp updatedAt =
                rs.getTimestamp("UPDATED_AT");

        classSubject.setUpdatedAt(
                updatedAt != null
                        ? updatedAt.toLocalDateTime()
                        : null
        );

        return classSubject;
    }
}
