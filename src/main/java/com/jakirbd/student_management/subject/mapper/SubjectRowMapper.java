package com.jakirbd.student_management.subject.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.jakirbd.student_management.subject.model.Subject;

public class SubjectRowMapper implements RowMapper<Subject> {

    @Override
    public Subject mapRow(ResultSet rs, int rowNum) throws SQLException {

        Subject subject = new Subject();

        subject.setSubjectId(rs.getLong("SUBJECT_ID"));
        subject.setSubjectName(rs.getString("SUBJECT_NAME"));
        subject.setSubjectCode(rs.getString("SUBJECT_CODE"));
        subject.setSubjectType(rs.getString("SUBJECT_TYPE"));
        subject.setDescription(rs.getString("DESCRIPTION"));

        int displayOrder = rs.getInt("DISPLAY_ORDER");
        subject.setDisplayOrder(
                rs.wasNull() ? null : displayOrder
        );

        subject.setStatus(rs.getString("STATUS"));

        Timestamp createdAt = rs.getTimestamp("CREATED_AT");
        subject.setCreatedAt(
                createdAt != null
                        ? createdAt.toLocalDateTime()
                        : null
        );

        Timestamp updatedAt = rs.getTimestamp("UPDATED_AT");
        subject.setUpdatedAt(
                updatedAt != null
                        ? updatedAt.toLocalDateTime()
                        : null
        );

        return subject;
    }
}
