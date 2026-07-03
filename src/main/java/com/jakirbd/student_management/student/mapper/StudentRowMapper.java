package com.jakirbd.student_management.student.mapper;

import com.jakirbd.student_management.student.model.Student;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class StudentRowMapper implements RowMapper<Student> {

    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Student student = new Student();

        student.setStudentId(rs.getLong("STUDENT_ID"));
        student.setStudentCode(rs.getString("STUDENT_CODE"));
        student.setFirstName(rs.getString("FIRST_NAME"));
        student.setLastName(rs.getString("LAST_NAME"));
        student.setEmail(rs.getString("EMAIL"));
        student.setPhone(rs.getString("PHONE"));
        student.setGender(rs.getString("GENDER"));

        if (rs.getDate("DATE_OF_BIRTH") != null) {
            student.setDateOfBirth(rs.getDate("DATE_OF_BIRTH").toLocalDate());
        }

        if (rs.getDate("ADMISSION_DATE") != null) {
            student.setAdmissionDate(rs.getDate("ADMISSION_DATE").toLocalDate());
        }

        student.setStatus(rs.getString("STATUS"));

        Timestamp createdAt = rs.getTimestamp("CREATED_AT");
        if (createdAt != null) {
            student.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("UPDATED_AT");
        if (updatedAt != null) {
            student.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return student;
    }
}