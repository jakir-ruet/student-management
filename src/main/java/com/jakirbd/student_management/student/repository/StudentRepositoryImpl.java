package com.jakirbd.student_management.student.repository;

import com.jakirbd.student_management.student.mapper.StudentRowMapper;
import com.jakirbd.student_management.student.model.Student;

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
public class StudentRepositoryImpl implements StudentRepository {

    private final JdbcTemplate jdbcTemplate;
    private final StudentRowMapper studentRowMapper;

    public StudentRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.studentRowMapper = new StudentRowMapper();
    }

    @Override
    public Long addStudent(Student student) {

        return jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {

                    CallableStatement cs = connection.prepareCall(
                            "{ call STUDENT_PKG.ADD_STUDENT(?, ?, ?, ?, ?, ?, ?, ?, ?) }"
                    );

                    cs.setString(1, student.getStudentCode());
                    cs.setString(2, student.getFirstName());
                    cs.setString(3, student.getLastName());
                    cs.setString(4, student.getEmail());
                    cs.setString(5, student.getPhone());
                    cs.setString(6, student.getGender());

                    if (student.getDateOfBirth() != null) {
                        cs.setDate(
                                7,
                                Date.valueOf(student.getDateOfBirth())
                        );
                    } else {
                        cs.setNull(7, Types.DATE);
                    }

                    if (student.getAdmissionDate() != null) {
                        cs.setDate(
                                8,
                                Date.valueOf(student.getAdmissionDate())
                        );
                    } else {
                        cs.setNull(8, Types.DATE);
                    }

                    cs.registerOutParameter(9, Types.NUMERIC);

                    return cs;
                },
                (CallableStatementCallback<Long>) cs -> {
                    cs.execute();
                    return cs.getLong(9);
                }
        );
    }

    @Override
    public void updateStudent(Student student) {

        jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {

                    CallableStatement cs = connection.prepareCall(
                            "{ call STUDENT_PKG.UPDATE_STUDENT(?, ?, ?, ?, ?, ?, ?, ?, ?) }"
                    );

                    cs.setLong(1, student.getStudentId());
                    cs.setString(2, student.getFirstName());
                    cs.setString(3, student.getLastName());
                    cs.setString(4, student.getEmail());
                    cs.setString(5, student.getPhone());
                    cs.setString(6, student.getGender());

                    if (student.getDateOfBirth() != null) {
                        cs.setDate(
                                7,
                                Date.valueOf(student.getDateOfBirth())
                        );
                    } else {
                        cs.setNull(7, Types.DATE);
                    }

                    if (student.getAdmissionDate() != null) {
                        cs.setDate(
                                8,
                                Date.valueOf(student.getAdmissionDate())
                        );
                    } else {
                        cs.setNull(8, Types.DATE);
                    }

                    cs.setString(9, student.getStatus());

                    return cs;
                },
                (CallableStatementCallback<Void>) cs -> {
                    cs.execute();
                    return null;
                }
        );
    }

    @Override
    public void deleteStudent(Long studentId) {

        jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {

                    CallableStatement cs = connection.prepareCall(
                            "{ call STUDENT_PKG.DELETE_STUDENT(?) }"
                    );

                    cs.setLong(1, studentId);

                    return cs;
                },
                (CallableStatementCallback<Void>) cs -> {
                    cs.execute();
                    return null;
                }
        );
    }

    @Override
    public Optional<Student> getStudentById(Long studentId) {

        return jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {

                    CallableStatement cs = connection.prepareCall(
                            "{ call STUDENT_PKG.GET_STUDENT_BY_ID(?, ?) }"
                    );

                    cs.setLong(1, studentId);
                    cs.registerOutParameter(
                            2,
                            OracleTypes.CURSOR
                    );

                    return cs;
                },
                (CallableStatementCallback<Optional<Student>>) cs -> {

                    cs.execute();

                    try (ResultSet rs =
                                 (ResultSet) cs.getObject(2)) {

                        if (rs.next()) {
                            Student student =
                                    studentRowMapper.mapRow(rs, 0);

                            return Optional.of(student);
                        }

                        return Optional.empty();
                    }
                }
        );
    }

    @Override
    public List<Student> getAllStudents() {

        return jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {

                    CallableStatement cs = connection.prepareCall(
                            "{ call STUDENT_PKG.GET_ALL_STUDENTS(?) }"
                    );

                    cs.registerOutParameter(
                            1,
                            OracleTypes.CURSOR
                    );

                    return cs;
                },
                (CallableStatementCallback<List<Student>>) cs -> {

                    cs.execute();

                    List<Student> students = new ArrayList<>();

                    try (ResultSet rs =
                                 (ResultSet) cs.getObject(1)) {

                        int rowNum = 0;

                        while (rs.next()) {
                            students.add(
                                    studentRowMapper.mapRow(
                                            rs,
                                            rowNum++
                                    )
                            );
                        }
                    }

                    return students;
                }
        );
    }

    @Override
    public List<Student> searchStudents(String searchText) {

        return jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {

                    CallableStatement cs = connection.prepareCall(
                            "{ call STUDENT_PKG.SEARCH_STUDENTS(?, ?) }"
                    );

                    cs.setString(1, searchText);

                    cs.registerOutParameter(
                            2,
                            OracleTypes.CURSOR
                    );

                    return cs;
                },
                (CallableStatementCallback<List<Student>>) cs -> {

                    cs.execute();

                    List<Student> students = new ArrayList<>();

                    try (ResultSet rs =
                                 (ResultSet) cs.getObject(2)) {

                        int rowNum = 0;

                        while (rs.next()) {
                            students.add(
                                    studentRowMapper.mapRow(
                                            rs,
                                            rowNum++
                                    )
                            );
                        }
                    }

                    return students;
                }
        );
    }
}
