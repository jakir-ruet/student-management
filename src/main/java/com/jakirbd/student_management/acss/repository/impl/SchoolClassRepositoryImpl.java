package com.jakirbd.student_management.acss.repository.impl;

import com.jakirbd.student_management.acss.mapper.SchoolClassRowMapper;
import com.jakirbd.student_management.acss.model.SchoolClass;
import com.jakirbd.student_management.acss.repository.SchoolClassRepository;
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
public class SchoolClassRepositoryImpl implements SchoolClassRepository {

    private final JdbcTemplate jdbcTemplate;

    public SchoolClassRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long createClass(String className, String classCode, Integer displayOrder, String status) {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.ADD_CLASS(?, ?, ?, ?, ?)}"
            );

            cs.setString(1, className);
            cs.setString(2, classCode);
            cs.setInt(3, displayOrder);
            cs.setString(4, status);
            cs.registerOutParameter(5, OracleTypes.NUMBER);

            return cs;
        };

        CallableStatementCallback<Long> callback = cs -> {
            cs.execute();
            return cs.getLong(5);
        };

        return jdbcTemplate.execute(creator, callback);
    }

    @Override
    public void updateClass(Long classId, String className, String classCode, Integer displayOrder, String status) {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.UPDATE_CLASS(?, ?, ?, ?, ?)}"
            );

            cs.setLong(1, classId);
            cs.setString(2, className);
            cs.setString(3, classCode);
            cs.setInt(4, displayOrder);
            cs.setString(5, status);

            return cs;
        };

        CallableStatementCallback<Boolean> callback = CallableStatement::execute;
        jdbcTemplate.execute(creator, callback);
    }

    @Override
    public Optional<SchoolClass> findClassById(Long classId) {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.GET_CLASS_BY_ID(?, ?)}"
            );

            cs.setLong(1, classId);
            cs.registerOutParameter(2, OracleTypes.CURSOR);

            return cs;
        };

        CallableStatementCallback<Optional<SchoolClass>> callback = cs -> {
            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                SchoolClassRowMapper rowMapper = new SchoolClassRowMapper();

                if (rs.next()) {
                    return Optional.of(rowMapper.mapRow(rs, 1));
                }

                return Optional.empty();
            }
        };

        return jdbcTemplate.execute(creator, callback);
    }

    @Override
    public List<SchoolClass> findAllClasses() {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.GET_ALL_CLASSES(?)}"
            );

            cs.registerOutParameter(1, OracleTypes.CURSOR);

            return cs;
        };

        CallableStatementCallback<List<SchoolClass>> callback = cs -> {
            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(1)) {
                SchoolClassRowMapper rowMapper = new SchoolClassRowMapper();
                List<SchoolClass> classes = new ArrayList<>();

                int rowNum = 0;
                while (rs.next()) {
                    classes.add(rowMapper.mapRow(rs, rowNum++));
                }

                return classes;
            }
        };

        return jdbcTemplate.execute(creator, callback);
    }

    @Override
    public void deleteClass(Long classId) {
        CallableStatementCreator creator = connection -> {
            CallableStatement cs = connection.prepareCall(
                    "{call ACSS_MGT_PKG.DELETE_CLASS(?)}"
            );

            cs.setLong(1, classId);

            return cs;
        };

        CallableStatementCallback<Boolean> callback = CallableStatement::execute;
        jdbcTemplate.execute(creator, callback);
    }
}
