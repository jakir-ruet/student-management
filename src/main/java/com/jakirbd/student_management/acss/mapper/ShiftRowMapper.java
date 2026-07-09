package com.jakirbd.student_management.acss.mapper;

import com.jakirbd.student_management.acss.model.Shift;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShiftRowMapper implements RowMapper<Shift> {

    @Override
    public Shift mapRow(ResultSet rs, int rowNum) throws SQLException {

        Shift shift = new Shift();

        shift.setShiftId(rs.getLong("SHIFT_ID"));
        shift.setShiftName(rs.getString("SHIFT_NAME"));
        shift.setShiftCode(rs.getString("SHIFT_CODE"));
        shift.setStartTime(rs.getString("START_TIME"));
        shift.setEndTime(rs.getString("END_TIME"));
        shift.setDisplayOrder(rs.getInt("DISPLAY_ORDER"));
        shift.setStatus(rs.getString("STATUS"));

        if (rs.getTimestamp("CREATED_AT") != null) {
            shift.setCreatedAt(rs.getTimestamp("CREATED_AT").toLocalDateTime());
        }

        if (rs.getTimestamp("UPDATED_AT") != null) {
            shift.setUpdatedAt(rs.getTimestamp("UPDATED_AT").toLocalDateTime());
        }

        return shift;
    }
}
