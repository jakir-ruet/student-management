package com.jakirbd.student_management.student.mapper;

import com.jakirbd.student_management.student.model.StudentGuardian;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class StudentGuardianRowMapper implements RowMapper<StudentGuardian> {

    @Override
    public StudentGuardian mapRow(ResultSet rs, int rowNum) throws SQLException {

        StudentGuardian guardian = new StudentGuardian();

        guardian.setGuardianId(rs.getLong("GUARDIAN_ID"));
        guardian.setStudentId(rs.getLong("STUDENT_ID"));
        guardian.setGuardianName(rs.getString("GUARDIAN_NAME"));
        guardian.setRelationship(rs.getString("RELATIONSHIP"));
        guardian.setPhone(rs.getString("PHONE"));
        guardian.setEmail(rs.getString("EMAIL"));
        guardian.setOccupation(rs.getString("OCCUPATION"));
        guardian.setIsPrimary(rs.getString("IS_PRIMARY"));

        Timestamp createdAt = rs.getTimestamp("CREATED_AT");
        if (createdAt != null) {
            guardian.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("UPDATED_AT");
        if (updatedAt != null) {
            guardian.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return guardian;
    }
}