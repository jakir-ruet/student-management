package com.jakirbd.student_management.teacher.repository;

import com.jakirbd.student_management.teacher.mapper.TeacherRowMapper;
import com.jakirbd.student_management.teacher.model.Teacher;
import oracle.jdbc.OracleTypes;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TeacherRepositoryImpl implements TeacherRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TeacherRowMapper teacherRowMapper;

    public TeacherRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.teacherRowMapper = new TeacherRowMapper();
    }

    @Override
    public Long create(Teacher teacher) {
        return jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(
                            "{ call TEACHER_PKG.ADD_TEACHER(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }"
                    );

                    cs.setString(1, teacher.getTeacherCode());
                    cs.setString(2, teacher.getFirstName());
                    cs.setString(3, teacher.getLastName());
                    cs.setString(4, teacher.getEmail());
                    cs.setString(5, teacher.getPhone());
                    cs.setString(6, teacher.getGender());
                    cs.setDate(7, Date.valueOf(teacher.getDateOfBirth()));
                    cs.setDate(8, Date.valueOf(teacher.getJoiningDate()));
                    cs.setString(9, teacher.getDesignation());
                    cs.setString(10, teacher.getQualification());
                    cs.setString(11, teacher.getDepartment());
                    cs.registerOutParameter(12, Types.NUMERIC);

                    return cs;
                },
                (CallableStatementCallback<Long>) cs -> {
                    cs.execute();
                    return cs.getLong(12);
                }
        );
    }

    @Override
    public void update(Teacher teacher) {
        jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(
                            "{ call TEACHER_PKG.UPDATE_TEACHER(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }"
                    );

                    cs.setLong(1, teacher.getTeacherId());
                    cs.setString(2, teacher.getFirstName());
                    cs.setString(3, teacher.getLastName());
                    cs.setString(4, teacher.getEmail());
                    cs.setString(5, teacher.getPhone());
                    cs.setString(6, teacher.getGender());
                    cs.setDate(7, Date.valueOf(teacher.getDateOfBirth()));
                    cs.setDate(8, Date.valueOf(teacher.getJoiningDate()));
                    cs.setString(9, teacher.getDesignation());
                    cs.setString(10, teacher.getQualification());
                    cs.setString(11, teacher.getDepartment());
                    cs.setString(12, teacher.getStatus());

                    return cs;
                },
                (CallableStatementCallback<Void>) cs -> {
                    cs.execute();
                    return null;
                }
        );
    }

    @Override
    public void delete(Long teacherId) {
        jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(
                            "{ call TEACHER_PKG.DELETE_TEACHER(?) }"
                    );

                    cs.setLong(1, teacherId);
                    return cs;
                },
                (CallableStatementCallback<Void>) cs -> {
                    cs.execute();
                    return null;
                }
        );
    }

    @Override
    public Optional<Teacher> findById(Long teacherId) {
        return jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(
                            "{ call TEACHER_PKG.GET_TEACHER_BY_ID(?, ?) }"
                    );

                    cs.setLong(1, teacherId);
                    cs.registerOutParameter(2, OracleTypes.CURSOR);

                    return cs;
                },
                (CallableStatementCallback<Optional<Teacher>>) cs -> {
                    cs.execute();

                    try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                        if (rs.next()) {
                            return Optional.of(teacherRowMapper.mapRow(rs, 0));
                        }

                        return Optional.empty();
                    }
                }
        );
    }

    @Override
    public List<Teacher> findAll() {
        return jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(
                            "{ call TEACHER_PKG.GET_ALL_TEACHERS(?) }"
                    );

                    cs.registerOutParameter(1, OracleTypes.CURSOR);
                    return cs;
                },
                (CallableStatementCallback<List<Teacher>>) cs -> {
                    cs.execute();

                    List<Teacher> teachers = new ArrayList<>();

                    try (ResultSet rs = (ResultSet) cs.getObject(1)) {
                        int rowNum = 0;

                        while (rs.next()) {
                            teachers.add(teacherRowMapper.mapRow(rs, rowNum++));
                        }
                    }

                    return teachers;
                }
        );
    }

    @Override
    public List<Teacher> search(String searchText) {
        return jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(
                            "{ call TEACHER_PKG.SEARCH_TEACHERS(?, ?) }"
                    );

                    cs.setString(1, searchText);
                    cs.registerOutParameter(2, OracleTypes.CURSOR);

                    return cs;
                },
                (CallableStatementCallback<List<Teacher>>) cs -> {
                    cs.execute();

                    List<Teacher> teachers = new ArrayList<>();

                    try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                        int rowNum = 0;

                        while (rs.next()) {
                            teachers.add(teacherRowMapper.mapRow(rs, rowNum++));
                        }
                    }

                    return teachers;
                }
        );
    }
}