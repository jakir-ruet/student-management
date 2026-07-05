package com.jakirbd.student_management.teacher.mapper;

import com.jakirbd.student_management.teacher.model.TeacherContact;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TeacherContactRowMapper implements RowMapper<TeacherContact> {

    @Override
    public TeacherContact mapRow(ResultSet rs, int rowNum) throws SQLException {
        TeacherContact teacherCoontact = new TeacherContact();

        teacherCoontact.setContactId(rs.getLong("CONTACT_ID"));
        teacherCoontact.setTeacherId(rs.getLong("TEACHER_ID"));
        teacherCoontact.setContactName(rs.getString("CONTACT_NAME"));
        teacherCoontact.setRelationship(rs.getString("RELATIONSHIP"));
        teacherCoontact.setPhone(rs.getString("PHONE"));
        teacherCoontact.setEmail(rs.getString("EMAIL"));
        teacherCoontact.setIsPrimary(rs.getString("IS_PRIMARY"));

        Timestamp createdAt = rs.getTimestamp("CREATED_AT");
        if (createdAt != null) {
            teacherCoontact.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("UPDATED_AT");
        if (updatedAt != null) {
            teacherCoontact.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return teacherCoontact;
    }
}