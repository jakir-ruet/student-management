package com.jakirbd.student_management.student.mapper;

import com.jakirbd.student_management.student.model.StudentAddress;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class StudentAddressRowMapper implements RowMapper<StudentAddress> {

    @Override
    public StudentAddress mapRow(ResultSet rs, int rowNum) throws SQLException {

        StudentAddress address = new StudentAddress();

        address.setAddressId(rs.getLong("ADDRESS_ID"));
        address.setStudentId(rs.getLong("STUDENT_ID"));
        address.setAddressType(rs.getString("ADDRESS_TYPE"));
        address.setAddressLine(rs.getString("ADDRESS_LINE"));
        address.setCity(rs.getString("CITY"));
        address.setDistrict(rs.getString("DISTRICT"));
        address.setPostalCode(rs.getString("POSTAL_CODE"));
        address.setCountry(rs.getString("COUNTRY"));

        Timestamp createdAt = rs.getTimestamp("CREATED_AT");
        if (createdAt != null) {
            address.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("UPDATED_AT");
        if (updatedAt != null) {
            address.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return address;
    }
}