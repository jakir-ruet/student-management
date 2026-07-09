package com.jakirbd.student_management.acss.mapper;

import com.jakirbd.student_management.acss.model.Section;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SectionRowMapper implements RowMapper<Section> {

    @Override
    public Section mapRow(ResultSet rs, int rowNum) throws SQLException {

        Section section = new Section();

        section.setSectionId(
                rs.getLong("SECTION_ID")
        );

        section.setAcademicYearClassId(
                rs.getLong("ACADEMIC_YEAR_CLASS_ID")
        );

        section.setSectionName(
                rs.getString("SECTION_NAME")
        );

        section.setSectionCode(
                rs.getString("SECTION_CODE")
        );

        Integer capacity = rs.getObject(
                "CAPACITY",
                Integer.class
        );
        section.setCapacity(capacity);

        section.setDisplayOrder(
                rs.getInt("DISPLAY_ORDER")
        );

        section.setStatus(
                rs.getString("STATUS")
        );

        if (rs.getTimestamp("CREATED_AT") != null) {
            section.setCreatedAt(
                    rs.getTimestamp("CREATED_AT").toLocalDateTime()
            );
        }

        if (rs.getTimestamp("UPDATED_AT") != null) {
            section.setUpdatedAt(
                    rs.getTimestamp("UPDATED_AT").toLocalDateTime()
            );
        }

        return section;
    }
}
