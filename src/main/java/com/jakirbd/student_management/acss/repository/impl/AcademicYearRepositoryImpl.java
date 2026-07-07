package com.jakirbd.student_management.acss.repository.impl;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.jakirbd.student_management.acss.mapper.AcademicYearRowMapper;
import com.jakirbd.student_management.acss.model.AcademicYear;
import com.jakirbd.student_management.acss.repository.AcademicYearRepository;

import oracle.jdbc.OracleTypes;

@Repository
public class AcademicYearRepositoryImpl implements AcademicYearRepository {

    private final JdbcTemplate jdbcTemplate;

    public AcademicYearRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long createAcademicYear(
            String yearName,
            LocalDate startDate,
            LocalDate endDate,
            String isCurrent,
            String status
    ) {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.ADD_ACADEMIC_YEAR(?, ?, ?, ?, ?, ?)}"
            );

            cs.setString(1, yearName);
            cs.setDate(2, Date.valueOf(startDate));
            cs.setDate(3, Date.valueOf(endDate));
            cs.setString(4, isCurrent);
            cs.setString(5, status);
            cs.registerOutParameter(6, OracleTypes.NUMBER);

            return cs;
        };

        CallableStatementCallback<Long> callback = cs -> {
            cs.execute();
            return cs.getLong(6);
        };

        return jdbcTemplate.execute(creator, callback);
    }

    @Override
    public void updateAcademicYear(
            Long academicYearId,
            String yearName,
            LocalDate startDate,
            LocalDate endDate,
            String isCurrent,
            String status
    ) {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.UPDATE_ACADEMIC_YEAR(?, ?, ?, ?, ?, ?)}"
            );

            cs.setLong(1, academicYearId);
            cs.setString(2, yearName);
            cs.setDate(3, Date.valueOf(startDate));
            cs.setDate(4, Date.valueOf(endDate));
            cs.setString(5, isCurrent);
            cs.setString(6, status);

            return cs;
        };

        CallableStatementCallback<Boolean> callback = CallableStatement::execute;

        jdbcTemplate.execute(creator, callback);
    }

    @Override
    public Optional<AcademicYear> findAcademicYearById(Long academicYearId) {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.GET_ACADEMIC_YEAR_BY_ID(?, ?)}"
            );

            cs.setLong(1, academicYearId);
            cs.registerOutParameter(2, OracleTypes.CURSOR);

            return cs;
        };

        CallableStatementCallback<Optional<AcademicYear>> callback = cs -> {
            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                AcademicYearRowMapper rowMapper = new AcademicYearRowMapper();

                if (rs.next()) {
                    return Optional.of(rowMapper.mapRow(rs, 1));
                }

                return Optional.empty();
            }
        };

        return jdbcTemplate.execute(creator, callback);
    }

    @Override
    public List<AcademicYear> findAllAcademicYears() {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.GET_ALL_ACADEMIC_YEARS(?)}"
            );

            cs.registerOutParameter(1, OracleTypes.CURSOR);

            return cs;
        };

        CallableStatementCallback<List<AcademicYear>> callback = cs -> {
            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(1)) {
                AcademicYearRowMapper rowMapper = new AcademicYearRowMapper();
                List<AcademicYear> academicYears = new ArrayList<>();

                int rowNum = 0;
                while (rs.next()) {
                    academicYears.add(rowMapper.mapRow(rs, rowNum++));
                }

                return academicYears;
            }
        };

        return jdbcTemplate.execute(creator, callback);
    }

    @Override
    public void deleteAcademicYear(Long academicYearId) {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.DELETE_ACADEMIC_YEAR(?)}"
            );

            cs.setLong(1, academicYearId);

            return cs;
        };

        CallableStatementCallback<Boolean> callback = CallableStatement::execute;

        jdbcTemplate.execute(creator, callback);
    }
}
