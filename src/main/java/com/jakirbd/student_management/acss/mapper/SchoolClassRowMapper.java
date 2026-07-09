package com.jakirbd.student_management.acss.mapper;

import com.jakirbd.student_management.acss.model.SchoolClass;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SchoolClassRowMapper implements RowMapper<SchoolClass> {

    @Override
    public SchoolClass mapRow(ResultSet rs, int rowNum) throws SQLException {

        SchoolClass schoolClass = new SchoolClass();

        schoolClass.setClassId(
                rs.getLong("CLASS_ID")
        );

        schoolClass.setClassName(
                rs.getString("CLASS_NAME")
        );

        schoolClass.setClassCode(
                rs.getString("CLASS_CODE")
        );

        schoolClass.setDisplayOrder(
                rs.getInt("DISPLAY_ORDER")
        );

        schoolClass.setStatus(
                rs.getString("STATUS")
        );

        if (rs.getTimestamp("CREATED_AT") != null) {
            schoolClass.setCreatedAt(
                    rs.getTimestamp("CREATED_AT").toLocalDateTime()
            );
        }

        if (rs.getTimestamp("UPDATED_AT") != null) {
            schoolClass.setUpdatedAt(
                    rs.getTimestamp("UPDATED_AT").toLocalDateTime()
            );
        }

        return schoolClass;
    }
}
