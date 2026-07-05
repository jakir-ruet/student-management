package com.jakirbd.student_management.teacher.repository;

import com.jakirbd.student_management.teacher.mapper.TeacherContactRowMapper;
import com.jakirbd.student_management.teacher.model.TeacherContact;
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

@Repository
public class TeacherContactRepositoryImpl implements TeacherContactRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TeacherContactRowMapper teacherContactRowMapper;

    public TeacherContactRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.teacherContactRowMapper = new TeacherContactRowMapper();
    }

    @Override
    public Long create(TeacherContact contact) {
        return jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(
                            "{ call TEACHER_CONTACT_PKG.ADD_CONTACT(?, ?, ?, ?, ?, ?, ?) }"
                    );

                    cs.setLong(1, contact.getTeacherId());
                    cs.setString(2, contact.getContactName());
                    cs.setString(3, contact.getRelationship());
                    cs.setString(4, contact.getPhone());
                    cs.setString(5, contact.getEmail());
                    cs.setString(6, contact.getIsPrimary());
                    cs.registerOutParameter(7, Types.NUMERIC);

                    return cs;
                },
                (CallableStatementCallback<Long>) cs -> {
                    cs.execute();
                    return cs.getLong(7);
                }
        );
    }

    @Override
    public void update(TeacherContact contact) {
        jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(
                            "{ call TEACHER_CONTACT_PKG.UPDATE_CONTACT(?, ?, ?, ?, ?, ?) }"
                    );

                    cs.setLong(1, contact.getContactId());
                    cs.setString(2, contact.getContactName());
                    cs.setString(3, contact.getRelationship());
                    cs.setString(4, contact.getPhone());
                    cs.setString(5, contact.getEmail());
                    cs.setString(6, contact.getIsPrimary());

                    return cs;
                },
                (CallableStatementCallback<Void>) cs -> {
                    cs.execute();
                    return null;
                }
        );
    }

    @Override
    public void delete(Long contactId) {
        jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(
                            "{ call TEACHER_CONTACT_PKG.DELETE_CONTACT(?) }"
                    );

                    cs.setLong(1, contactId);

                    return cs;
                },
                (CallableStatementCallback<Void>) cs -> {
                    cs.execute();
                    return null;
                }
        );
    }

    @Override
    public List<TeacherContact> findByTeacherId(Long teacherId) {
        return jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(
                            "{ call TEACHER_CONTACT_PKG.GET_CONTACTS_BY_TEACHER(?, ?) }"
                    );

                    cs.setLong(1, teacherId);
                    cs.registerOutParameter(2, OracleTypes.CURSOR);

                    return cs;
                },
                (CallableStatementCallback<List<TeacherContact>>) cs -> {
                    cs.execute();

                    List<TeacherContact> contacts = new ArrayList<>();

                    try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                        int rowNum = 0;

                        while (rs.next()) {
                            contacts.add(
                                    teacherContactRowMapper.mapRow(
                                            rs,
                                            rowNum++
                                    )
                            );
                        }
                    }

                    return contacts;
                }
        );
    }
}