package com.jakirbd.student_management.acss.mapper;

import com.jakirbd.student_management.acss.model.SectionShift;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SectionShiftRowMapper implements RowMapper<SectionShift> {

    @Override
    public SectionShift mapRow(ResultSet rs, int rowNum) throws SQLException {

        SectionShift sectionShift = new SectionShift();

        sectionShift.setSectionShiftId(
                rs.getLong("SECTION_SHIFT_ID")
        );

        sectionShift.setSectionId(
                rs.getLong("SECTION_ID")
        );

        sectionShift.setShiftId(
                rs.getLong("SHIFT_ID")
        );

        sectionShift.setStatus(
                rs.getString("STATUS")
        );

        if (rs.getTimestamp("CREATED_AT") != null) {
            sectionShift.setCreatedAt(
                    rs.getTimestamp("CREATED_AT").toLocalDateTime()
            );
        }

        if (rs.getTimestamp("UPDATED_AT") != null) {
            sectionShift.setUpdatedAt(
                    rs.getTimestamp("UPDATED_AT").toLocalDateTime()
            );
        }

        return sectionShift;
    }
}
