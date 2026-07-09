package com.jakirbd.student_management.acss.repository.impl;

import com.jakirbd.student_management.acss.dto.response.SectionResponse;
import com.jakirbd.student_management.acss.mapper.SectionResponseRowMapper;
import com.jakirbd.student_management.acss.mapper.SectionRowMapper;
import com.jakirbd.student_management.acss.model.Section;
import com.jakirbd.student_management.acss.repository.SectionRepository;
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
public class SectionRepositoryImpl implements SectionRepository {

    private final JdbcTemplate jdbcTemplate;

    public SectionRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long createSection(
            Long academicYearClassId,
            String sectionName,
            String sectionCode,
            Integer capacity,
            Integer displayOrder,
            String status
    ) {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.ADD_SECTION(?, ?, ?, ?, ?, ?, ?)}"
            );

            cs.setLong(1, academicYearClassId);
            cs.setString(2, sectionName);
            cs.setString(3, sectionCode);

            if (capacity == null) {
                cs.setNull(4, java.sql.Types.NUMERIC);
            } else {
                cs.setInt(4, capacity);
            }

            cs.setInt(5, displayOrder);
            cs.setString(6, status);
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
    public void updateSection(
            Long sectionId,
            Long academicYearClassId,
            String sectionName,
            String sectionCode,
            Integer capacity,
            Integer displayOrder,
            String status
    ) {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.UPDATE_SECTION(?, ?, ?, ?, ?, ?, ?)}"
            );

            cs.setLong(1, sectionId);
            cs.setLong(2, academicYearClassId);
            cs.setString(3, sectionName);
            cs.setString(4, sectionCode);

            if (capacity == null) {
                cs.setNull(5, java.sql.Types.NUMERIC);
            } else {
                cs.setInt(5, capacity);
            }

            cs.setInt(6, displayOrder);
            cs.setString(7, status);

            return cs;
        };

        CallableStatementCallback<Boolean> callback = CallableStatement::execute;
        jdbcTemplate.execute(creator, callback);
    }

    @Override
    public Optional<SectionResponse> findSectionById(Long sectionId) {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.GET_SECTION_BY_ID(?, ?)}"
            );

            cs.setLong(1, sectionId);
            cs.registerOutParameter(2, OracleTypes.CURSOR);

            return cs;
        };

        CallableStatementCallback<Optional<SectionResponse>> callback = cs -> {
            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                SectionResponseRowMapper rowMapper = new SectionResponseRowMapper();

                if (rs.next()) {
                    return Optional.of(rowMapper.mapRow(rs, 1));
                }

                return Optional.empty();
            }
        };

        return jdbcTemplate.execute(creator, callback);
    }

    @Override
    public List<Section> findSectionsByAcademicYearClass(Long academicYearClassId) {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.GET_SECTIONS_BY_ACADEMIC_YEAR_CLASS(?, ?)}"
            );

            cs.setLong(1, academicYearClassId);
            cs.registerOutParameter(2, OracleTypes.CURSOR);

            return cs;
        };

        CallableStatementCallback<List<Section>> callback = cs -> {
            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                SectionRowMapper rowMapper = new SectionRowMapper();
                List<Section> sections = new ArrayList<>();

                int rowNum = 0;
                while (rs.next()) {
                    sections.add(rowMapper.mapRow(rs, rowNum++));
                }

                return sections;
            }
        };

        return jdbcTemplate.execute(creator, callback);
    }

    @Override
    public List<SectionResponse> findAllSections() {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.GET_ALL_SECTIONS(?)}"
            );

            cs.registerOutParameter(1, OracleTypes.CURSOR);

            return cs;
        };

        CallableStatementCallback<List<SectionResponse>> callback = cs -> {
            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(1)) {
                SectionResponseRowMapper rowMapper = new SectionResponseRowMapper();
                List<SectionResponse> sections = new ArrayList<>();

                int rowNum = 0;
                while (rs.next()) {
                    sections.add(rowMapper.mapRow(rs, rowNum++));
                }

                return sections;
            }
        };

        return jdbcTemplate.execute(creator, callback);
    }

    @Override
    public void deleteSection(Long sectionId) {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.DELETE_SECTION(?)}"
            );

            cs.setLong(1, sectionId);

            return cs;
        };

        CallableStatementCallback<Boolean> callback = CallableStatement::execute;
        jdbcTemplate.execute(creator, callback);
    }
}
