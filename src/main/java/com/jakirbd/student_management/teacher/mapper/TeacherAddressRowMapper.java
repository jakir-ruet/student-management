package com.jakirbd.student_management.teacher.mapper;

import com.jakirbd.student_management.teacher.model.TeacherAddress;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TeacherAddressRowMapper implements RowMapper<TeacherAddress> {

    @Override
    public TeacherAddress mapRow(ResultSet rs, int rowNum) throws SQLException {
        TeacherAddress TeacherAddress = new TeacherAddress();

        TeacherAddress.setAddressId(rs.getLong("ADDRESS_ID"));
        TeacherAddress.setTeacherId(rs.getLong("TEACHER_ID"));
        TeacherAddress.setAddressType(rs.getString("ADDRESS_TYPE"));
        TeacherAddress.setAddressLine(rs.getString("ADDRESS_LINE"));
        TeacherAddress.setCity(rs.getString("CITY"));
        TeacherAddress.setDistrict(rs.getString("DISTRICT"));
        TeacherAddress.setPostalCode(rs.getString("POSTAL_CODE"));
        TeacherAddress.setCountry(rs.getString("COUNTRY"));

        Timestamp createdAt = rs.getTimestamp("CREATED_AT");
        if (createdAt != null) {
            TeacherAddress.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("UPDATED_AT");
        if (updatedAt != null) {
            TeacherAddress.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return TeacherAddress;
    }
}