package com.jakirbd.student_management.acss.mapper;

import com.jakirbd.student_management.acss.model.AcademicYearClass;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AcademicYearClassRowMapper implements RowMapper<AcademicYearClass> {

    @Override
    public AcademicYearClass mapRow(ResultSet rs, int rowNum) throws SQLException {

        AcademicYearClass academicYearClass = new AcademicYearClass();

        academicYearClass.setAcademicYearClassId(
                rs.getLong("ACADEMIC_YEAR_CLASS_ID")
        );

        academicYearClass.setAcademicYearId(
                rs.getLong("ACADEMIC_YEAR_ID")
        );

        academicYearClass.setClassId(
                rs.getLong("CLASS_ID")
        );

        academicYearClass.setStatus(
                rs.getString("STATUS")
        );

        if (rs.getTimestamp("CREATED_AT") != null) {
            academicYearClass.setCreatedAt(
                    rs.getTimestamp("CREATED_AT").toLocalDateTime()
            );
        }

        if (rs.getTimestamp("UPDATED_AT") != null) {
            academicYearClass.setUpdatedAt(
                    rs.getTimestamp("UPDATED_AT").toLocalDateTime()
            );
        }

        return academicYearClass;
    }
}
