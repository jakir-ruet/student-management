package com.jakirbd.student_management.acss.repository.impl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.jakirbd.student_management.acss.mapper.ShiftRowMapper;
import com.jakirbd.student_management.acss.model.Shift;
import com.jakirbd.student_management.acss.repository.ShiftRepository;

import oracle.jdbc.OracleTypes;

@Repository
public class ShiftRepositoryImpl implements ShiftRepository {
	private final JdbcTemplate jdbcTemplate;

	public ShiftRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Long createShift(String shiftName, String shiftCode, String startTime, String endTime, Integer displayOrder,
			String status) {
		CallableStatementCreator creator = connection -> {
			CallableStatement cs = connection.prepareCall(
					"{call ACSS_MGT_PKG.ADD_SHIFT(?, ?, ?, ?, ?, ?, ?)}");
			cs.setString(1, shiftName);
			cs.setString(2, shiftCode);
			cs.setString(3, startTime);
			cs.setString(4, endTime);
			cs.setInt(5, displayOrder);
			cs.registerOutParameter(7, OracleTypes.NUMBER);
			return cs;
		};
		CallableStatementCallback<Long> callback = cs -> {
			cs.execute();
			return cs.getLong(7);
		};
		return jdbcTemplate.execute(creator, callback);
	}

	@Override
	public void updateShift(Long shiftId, String shiftName, String shiftCode, String startTime, String endTime,
			Integer displayOrder, String status) {
		CallableStatementCreator creator = connection -> {
			CallableStatement cs = connection.prepareCall(
					"{call ACSS_MGT_PKG.UPDATE_SHIFT(?, ?, ?, ?, ?, ?, ?)}");

			cs.setLong(1, shiftId);
			cs.setString(2, shiftName);
			cs.setString(3, shiftCode);
			cs.setString(4, startTime);
			cs.setString(5, endTime);
			cs.setInt(6, displayOrder);
			cs.setString(7, status);

			return cs;
		};
		CallableStatementCallback<Boolean> callback = CallableStatement::execute;
		jdbcTemplate.execute(creator, callback);
	}

	@Override
    public Optional<Shift> findShiftById(Long shiftId) {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.GET_SHIFT_BY_ID(?, ?)}"
            );

            cs.setLong(1, shiftId);
            cs.registerOutParameter(2, OracleTypes.CURSOR);

            return cs;
        };

        CallableStatementCallback<Optional<Shift>> callback = cs -> {
            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                ShiftRowMapper rowMapper = new ShiftRowMapper();

                if (rs.next()) {
                    return Optional.of(rowMapper.mapRow(rs, 1));
                }

                return Optional.empty();
            }
        };

        return jdbcTemplate.execute(creator, callback);
    }

    @Override
    public List<Shift> findAllShifts() {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.GET_ALL_SHIFTS(?)}"
            );

            cs.registerOutParameter(1, OracleTypes.CURSOR);

            return cs;
        };

        CallableStatementCallback<List<Shift>> callback = cs -> {
            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(1)) {
                ShiftRowMapper rowMapper = new ShiftRowMapper();
                List<Shift> shifts = new ArrayList<>();

                int rowNum = 0;
                while (rs.next()) {
                    shifts.add(rowMapper.mapRow(rs, rowNum++));
                }

                return shifts;
            }
        };

        return jdbcTemplate.execute(creator, callback);
    }

    @Override
    public void deleteShift(Long shiftId) {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.DELETE_SHIFT(?)}"
            );

            cs.setLong(1, shiftId);

            return cs;
        };

        CallableStatementCallback<Boolean> callback = CallableStatement::execute;
        jdbcTemplate.execute(creator, callback);
    }
}
