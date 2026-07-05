package com.jakirbd.student_management.teacher.mapper;

import com.jakirbd.student_management.teacher.model.Teacher;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TeacherRowMapper implements RowMapper<Teacher> {

    @Override
    public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
        Teacher teacher = new Teacher();

        teacher.setTeacherId(rs.getLong("TEACHER_ID"));
        teacher.setTeacherCode(rs.getString("TEACHER_CODE"));
        teacher.setFirstName(rs.getString("FIRST_NAME"));
        teacher.setLastName(rs.getString("LAST_NAME"));
        teacher.setEmail(rs.getString("EMAIL"));
        teacher.setPhone(rs.getString("PHONE"));
        teacher.setGender(rs.getString("GENDER"));

        if (rs.getDate("DATE_OF_BIRTH") != null) {
            teacher.setDateOfBirth(rs.getDate("DATE_OF_BIRTH").toLocalDate());
        }

        if (rs.getDate("JOINING_DATE") != null) {
            teacher.setJoiningDate(rs.getDate("JOINING_DATE").toLocalDate());
        }

        teacher.setDesignation(rs.getString("DESIGNATION"));
        teacher.setQualification(rs.getString("QUALIFICATION"));
        teacher.setDepartment(rs.getString("DEPARTMENT"));
        teacher.setStatus(rs.getString("STATUS"));

        Timestamp createdAt = rs.getTimestamp("CREATED_AT");
        if (createdAt != null) {
            teacher.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("UPDATED_AT");
        if (updatedAt != null) {
            teacher.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return teacher;
    }
}