package com.jakirbd.student_management.student.repository;

import com.jakirbd.student_management.student.mapper.StudentGuardianRowMapper;
import com.jakirbd.student_management.student.model.StudentGuardian;
import oracle.jdbc.OracleTypes;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class StudentGuardianRepositoryImpl implements StudentGuardianRepository {

    private final JdbcTemplate jdbcTemplate;
    private final StudentGuardianRowMapper guardianRowMapper;

    public StudentGuardianRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.guardianRowMapper = new StudentGuardianRowMapper();
    }

    @Override
    public Long addGuardian(StudentGuardian guardian) {
        return jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(
                            "{ call STUDENT_GUARDIAN_PKG.ADD_GUARDIAN(?, ?, ?, ?, ?, ?, ?, ?) }"
                    );

                    cs.setLong(1, guardian.getStudentId());
                    cs.setString(2, guardian.getGuardianName());
                    cs.setString(3, guardian.getRelationship());
                    cs.setString(4, guardian.getPhone());
                    cs.setString(5, guardian.getEmail());
                    cs.setString(6, guardian.getOccupation());
                    cs.setString(7, guardian.getIsPrimary());
                    cs.registerOutParameter(8, Types.NUMERIC);

                    return cs;
                },
                (CallableStatementCallback<Long>) cs -> {
                    cs.execute();
                    return cs.getLong(8);
                }
        );
    }

    @Override
    public void updateGuardian(StudentGuardian guardian) {
        jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(
                            "{ call STUDENT_GUARDIAN_PKG.UPDATE_GUARDIAN(?, ?, ?, ?, ?, ?, ?) }"
                    );

                    cs.setLong(1, guardian.getGuardianId());
                    cs.setString(2, guardian.getGuardianName());
                    cs.setString(3, guardian.getRelationship());
                    cs.setString(4, guardian.getPhone());
                    cs.setString(5, guardian.getEmail());
                    cs.setString(6, guardian.getOccupation());
                    cs.setString(7, guardian.getIsPrimary());

                    return cs;
                },
                (CallableStatementCallback<Void>) cs -> {
                    cs.execute();
                    return null;
                }
        );
    }

    @Override
    public void deleteGuardian(Long guardianId) {
        jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(
                            "{ call STUDENT_GUARDIAN_PKG.DELETE_GUARDIAN(?) }"
                    );

                    cs.setLong(1, guardianId);

                    return cs;
                },
                (CallableStatementCallback<Void>) cs -> {
                    cs.execute();
                    return null;
                }
        );
    }

    @Override
    public Optional<StudentGuardian> getGuardianById(Long guardianId) {
        return jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(
                            "{ call STUDENT_GUARDIAN_PKG.GET_GUARDIAN_BY_ID(?, ?) }"
                    );

                    cs.setLong(1, guardianId);
                    cs.registerOutParameter(2, OracleTypes.CURSOR);

                    return cs;
                },
                (CallableStatementCallback<Optional<StudentGuardian>>) cs -> {
                    cs.execute();

                    try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                        if (rs.next()) {
                            return Optional.of(guardianRowMapper.mapRow(rs, 0));
                        }

                        return Optional.empty();
                    }
                }
        );
    }

    @Override
    public List<StudentGuardian> getGuardiansByStudent(Long studentId) {
        return jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(
                            "{ call STUDENT_GUARDIAN_PKG.GET_GUARDIANS_BY_STUDENT(?, ?) }"
                    );

                    cs.setLong(1, studentId);
                    cs.registerOutParameter(2, OracleTypes.CURSOR);

                    return cs;
                },
                (CallableStatementCallback<List<StudentGuardian>>) cs -> {
                    cs.execute();

                    List<StudentGuardian> guardians = new ArrayList<>();

                    try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                        int rowNum = 0;

                        while (rs.next()) {
                            guardians.add(guardianRowMapper.mapRow(rs, rowNum++));
                        }
                    }

                    return guardians;
                }
        );
    }
}
