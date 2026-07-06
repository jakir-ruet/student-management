package com.jakirbd.student_management.acss.mapper;

import com.jakirbd.student_management.acss.dto.SectionShiftResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SectionShiftResponseRowMapper implements RowMapper<SectionShiftResponse> {

    @Override
    public SectionShiftResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        SectionShiftResponse response = new SectionShiftResponse();

        response.setSectionShiftId(rs.getLong("SECTION_SHIFT_ID"));

        response.setSectionId(rs.getLong("SECTION_ID"));
        response.setSectionName(rs.getString("SECTION_NAME"));
        response.setSectionCode(rs.getString("SECTION_CODE"));

        response.setShiftId(rs.getLong("SHIFT_ID"));
        response.setShiftName(rs.getString("SHIFT_NAME"));
        response.setShiftCode(rs.getString("SHIFT_CODE"));
        response.setStartTime(rs.getString("START_TIME"));
        response.setEndTime(rs.getString("END_TIME"));

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
