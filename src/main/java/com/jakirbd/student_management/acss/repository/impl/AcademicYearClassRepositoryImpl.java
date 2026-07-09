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

import com.jakirbd.student_management.acss.dto.response.AcademicYearClassResponse;
import com.jakirbd.student_management.acss.mapper.AcademicYearClassResponseRowMapper;
import com.jakirbd.student_management.acss.repository.AcademicYearClassRepository;

import oracle.jdbc.OracleTypes;

@Repository
public class AcademicYearClassRepositoryImpl implements AcademicYearClassRepository {

    private final JdbcTemplate jdbcTemplate;

    public AcademicYearClassRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
		public Long createAcademicYearClass(Long academicYearId, Long classId, String status) {
			CallableStatementCreator creator = connection -> {
				CallableStatement cs = connection.prepareCall(
						"{call ACSS_MGT_PKG.ADD_ACADEMIC_YEAR_CLASS(?, ?, ?, ?)}");

				cs.setLong(1, academicYearId);
				cs.setLong(2, classId);
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
    public void updateAcademicYearClass(
            Long academicYearClassId,
            Long academicYearId,
            Long classId,
            String status
    ) {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.UPDATE_ACADEMIC_YEAR_CLASS(?, ?, ?, ?)}"
            );

            cs.setLong(1, academicYearClassId);
            cs.setLong(2, academicYearId);
            cs.setLong(3, classId);
            cs.setString(4, status);

            return cs;
        };

        CallableStatementCallback<Boolean> callback = CallableStatement::execute;
        jdbcTemplate.execute(creator, callback);
    }

    @Override
    public Optional<AcademicYearClassResponse> findAcademicYearClassById(Long academicYearClassId) {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.GET_ACADEMIC_YEAR_CLASS_BY_ID(?, ?)}"
            );

            cs.setLong(1, academicYearClassId);
            cs.registerOutParameter(2, OracleTypes.CURSOR);

            return cs;
        };

        CallableStatementCallback<Optional<AcademicYearClassResponse>> callback = cs -> {
            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                AcademicYearClassResponseRowMapper rowMapper = new AcademicYearClassResponseRowMapper();

                if (rs.next()) {
                    return Optional.of(rowMapper.mapRow(rs, 1));
                }

                return Optional.empty();
            }
        };

        return jdbcTemplate.execute(creator, callback);
    }

    @Override
    public List<AcademicYearClassResponse> findClassesByAcademicYear(Long academicYearId) {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.GET_CLASSES_BY_ACADEMIC_YEAR(?, ?)}"
            );

            cs.setLong(1, academicYearId);
            cs.registerOutParameter(2, OracleTypes.CURSOR);

            return cs;
        };

        CallableStatementCallback<List<AcademicYearClassResponse>> callback = cs -> {
            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                AcademicYearClassResponseRowMapper rowMapper = new AcademicYearClassResponseRowMapper();
                List<AcademicYearClassResponse> responses = new ArrayList<>();

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
    public List<AcademicYearClassResponse> findAllAcademicYearClasses() {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.GET_ALL_ACADEMIC_YEAR_CLASSES(?)}"
            );

            cs.registerOutParameter(1, OracleTypes.CURSOR);

            return cs;
        };

        CallableStatementCallback<List<AcademicYearClassResponse>> callback = cs -> {
            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(1)) {
                AcademicYearClassResponseRowMapper rowMapper = new AcademicYearClassResponseRowMapper();
                List<AcademicYearClassResponse> responses = new ArrayList<>();

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
		public void deleteAcademicYearClass(Long academicYearClassId) {
			CallableStatementCreator creator = connection -> {
				CallableStatement cs = connection.prepareCall(
						"{call ACSS_MGT_PKG.DELETE_ACADEMIC_YEAR_CLASS(?)}");

				cs.setLong(1, academicYearClassId);

				return cs;
			};

			CallableStatementCallback<Boolean> callback = CallableStatement::execute;
			jdbcTemplate.execute(creator, callback);
		}
}
