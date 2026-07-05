package com.jakirbd.student_management.teacher.repository;

import com.jakirbd.student_management.teacher.mapper.TeacherAddressRowMapper;
import com.jakirbd.student_management.teacher.model.TeacherAddress;
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
public class TeacherAddressRepositoryImpl
        implements TeacherAddressRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TeacherAddressRowMapper teacherAddressRowMapper;

    public TeacherAddressRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.teacherAddressRowMapper = new TeacherAddressRowMapper();
    }

    @Override
    public Long create(TeacherAddress address) {
        return jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(
                            "{ call TEACHER_ADDRESS_PKG.ADD_ADDRESS(?, ?, ?, ?, ?, ?, ?, ?) }"
                    );

                    cs.setLong(1, address.getTeacherId());
                    cs.setString(2, address.getAddressType());
                    cs.setString(3, address.getAddressLine());
                    cs.setString(4, address.getCity());
                    cs.setString(5, address.getDistrict());
                    cs.setString(6, address.getPostalCode());
                    cs.setString(7, address.getCountry());
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
    public void update(TeacherAddress address) {
        jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(
                            "{ call TEACHER_ADDRESS_PKG.UPDATE_ADDRESS(?, ?, ?, ?, ?, ?, ?) }"
                    );

                    cs.setLong(1, address.getAddressId());
                    cs.setString(2, address.getAddressType());
                    cs.setString(3, address.getAddressLine());
                    cs.setString(4, address.getCity());
                    cs.setString(5, address.getDistrict());
                    cs.setString(6, address.getPostalCode());
                    cs.setString(7, address.getCountry());

                    return cs;
                },
                (CallableStatementCallback<Void>) cs -> {
                    cs.execute();
                    return null;
                }
        );
    }

    @Override
    public void delete(Long addressId) {
        jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(
                            "{ call TEACHER_ADDRESS_PKG.DELETE_ADDRESS(?) }"
                    );

                    cs.setLong(1, addressId);

                    return cs;
                },
                (CallableStatementCallback<Void>) cs -> {
                    cs.execute();
                    return null;
                }
        );
    }

    @Override
    public List<TeacherAddress> findByTeacherId(Long teacherId) {
        return jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(
                            "{ call TEACHER_ADDRESS_PKG.GET_ADDRESSES_BY_TEACHER(?, ?) }"
                    );

                    cs.setLong(1, teacherId);
                    cs.registerOutParameter(2, OracleTypes.CURSOR);

                    return cs;
                },
                (CallableStatementCallback<List<TeacherAddress>>) cs -> {
                    cs.execute();

                    List<TeacherAddress> addresses = new ArrayList<>();

                    try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                        int rowNum = 0;

                        while (rs.next()) {
                            addresses.add(
                                    teacherAddressRowMapper.mapRow(
                                            rs,
                                            rowNum++
                                    )
                            );
                        }
                    }

                    return addresses;
                }
        );
    }
}