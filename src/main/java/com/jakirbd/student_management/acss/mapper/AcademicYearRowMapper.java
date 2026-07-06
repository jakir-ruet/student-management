package com.jakirbd.student_management.acss.mapper;

import com.jakirbd.student_management.acss.model.AcademicYear;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AcademicYearRowMapper implements RowMapper<AcademicYear> {

    @Override
    public AcademicYear mapRow(ResultSet rs, int rowNum) throws SQLException {
        AcademicYear academicYear = new AcademicYear();

        academicYear.setAcademicYearId(rs.getLong("ACADEMIC_YEAR_ID"));
        academicYear.setYearName(rs.getString("YEAR_NAME"));
        academicYear.setStartDate(rs.getDate("START_DATE").toLocalDate());
        academicYear.setEndDate(rs.getDate("END_DATE").toLocalDate());
        academicYear.setIsCurrent(rs.getString("IS_CURRENT"));
        academicYear.setStatus(rs.getString("STATUS"));
        academicYear.setCreatedAt(rs.getTimestamp("CREATED_AT").toLocalDateTime());

        if (rs.getTimestamp("UPDATED_AT") != null) {
            academicYear.setUpdatedAt(rs.getTimestamp("UPDATED_AT").toLocalDateTime());
        }

        return academicYear;
    }
}
