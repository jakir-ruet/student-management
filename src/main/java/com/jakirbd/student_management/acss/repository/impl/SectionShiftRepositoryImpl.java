package com.jakirbd.student_management.acss.repository.impl;

import com.jakirbd.student_management.acss.dto.response.SectionShiftResponse;

import com.jakirbd.student_management.acss.mapper.SectionShiftResponseRowMapper;
import com.jakirbd.student_management.acss.repository.SectionShiftRepository;
import oracle.jdbc.OracleTypes;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class SectionShiftRepositoryImpl implements SectionShiftRepository {

    private final JdbcTemplate jdbcTemplate;

    public SectionShiftRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long createSectionShift(Long sectionId, Long shiftId, String status) {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.ADD_SECTION_SHIFT(?, ?, ?, ?)}"
            );

            cs.setLong(1, sectionId);
            cs.setLong(2, shiftId);
            cs.setString(3, status);
            cs.registerOutParameter(4, OracleTypes.NUMBER);

            return cs;
        };

        CallableStatementCallback<Long> callback = cs -> {
            cs.execute();
            return cs.getLong(4);
        };

        return jdbcTemplate.execute(creator, callback);
    }

    @Override
    public void updateSectionShift(Long sectionShiftId, Long sectionId, Long shiftId, String status) {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.UPDATE_SECTION_SHIFT(?, ?, ?, ?)}"
            );

            cs.setLong(1, sectionShiftId);
            cs.setLong(2, sectionId);
            cs.setLong(3, shiftId);
            cs.setString(4, status);

            return cs;
        };

        CallableStatementCallback<Boolean> callback = CallableStatement::execute;
        jdbcTemplate.execute(creator, callback);
    }

    @Override
    public Optional<SectionShiftResponse> findSectionShiftById(Long sectionShiftId) {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.GET_SECTION_SHIFT_BY_ID(?, ?)}"
            );

            cs.setLong(1, sectionShiftId);
            cs.registerOutParameter(2, OracleTypes.CURSOR);

            return cs;
        };

        CallableStatementCallback<Optional<SectionShiftResponse>> callback = cs -> {
            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                SectionShiftResponseRowMapper rowMapper = new SectionShiftResponseRowMapper();

                if (rs.next()) {
                    return Optional.of(rowMapper.mapRow(rs, 1));
                }

                return Optional.empty();
            }
        };

        return jdbcTemplate.execute(creator, callback);
    }

    @Override
    public List<SectionShiftResponse> findShiftsBySection(Long sectionId) {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.GET_SHIFTS_BY_SECTION(?, ?)}"
            );

            cs.setLong(1, sectionId);
            cs.registerOutParameter(2, OracleTypes.CURSOR);

            return cs;
        };

        CallableStatementCallback<List<SectionShiftResponse>> callback = cs -> {
            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                SectionShiftResponseRowMapper rowMapper = new SectionShiftResponseRowMapper();
                List<SectionShiftResponse> responses = new ArrayList<>();

                int rowNum = 0;
                while (rs.next()) {
                    responses.add(rowMapper.mapRow(rs, rowNum++));
                }

                return responses;
            }
        };

        return jdbcTemplate.execute(creator, callback);
    }

    @Override
    public List<SectionShiftResponse> findAllSectionShifts() {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.GET_ALL_SECTION_SHIFTS(?)}"
            );

            cs.registerOutParameter(1, OracleTypes.CURSOR);

            return cs;
        };

        CallableStatementCallback<List<SectionShiftResponse>> callback = cs -> {
            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(1)) {
                SectionShiftResponseRowMapper rowMapper = new SectionShiftResponseRowMapper();
                List<SectionShiftResponse> responses = new ArrayList<>();

                int rowNum = 0;
                while (rs.next()) {
                    responses.add(rowMapper.mapRow(rs, rowNum++));
                }

                return responses;
            }
        };

        return jdbcTemplate.execute(creator, callback);
    }

    @Override
    public void deleteSectionShift(Long sectionShiftId) {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.DELETE_SECTION_SHIFT(?)}"
            );

            cs.setLong(1, sectionShiftId);

            return cs;
        };

        CallableStatementCallback<Boolean> callback = CallableStatement::execute;
        jdbcTemplate.execute(creator, callback);
    }
}
