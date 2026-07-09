package com.jakirbd.student_management.acss.mapper;

import org.springframework.jdbc.core.RowMapper;

import com.jakirbd.student_management.acss.dto.response.AcademicYearClassResponse;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AcademicYearClassResponseRowMapper implements RowMapper<AcademicYearClassResponse> {

    @Override
    public AcademicYearClassResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        AcademicYearClassResponse response = new AcademicYearClassResponse();

        response.setAcademicYearClassId(rs.getLong("ACADEMIC_YEAR_CLASS_ID"));
        response.setAcademicYearId(rs.getLong("ACADEMIC_YEAR_ID"));
        response.setYearName(rs.getString("YEAR_NAME"));

        response.setClassId(rs.getLong("CLASS_ID"));
        response.setClassName(rs.getString("CLASS_NAME"));
        response.setClassCode(rs.getString("CLASS_CODE"));
        response.setClassDisplayOrder(rs.getInt("CLASS_DISPLAY_ORDER"));

        response.setStatus(rs.getString("STATUS"));

        if (rs.getTimestamp("CREATED_AT") != null) {
            response.setCreatedAt(rs.getTimestamp("CREATED_AT").toLocalDateTime());
        }

        if (rs.getTimestamp("UPDATED_AT") != null) {
            response.setUpdatedAt(rs.getTimestamp("UPDATED_AT").toLocalDateTime());
        }

        return response;
    }
}
