package com.jakirbd.student_management.student.repository;

import com.jakirbd.student_management.student.mapper.StudentAddressRowMapper;
import com.jakirbd.student_management.student.model.StudentAddress;
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
public class StudentAddressRepositoryImpl implements StudentAddressRepository {

    private final JdbcTemplate jdbcTemplate;
    private final StudentAddressRowMapper addressRowMapper;

    public StudentAddressRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.addressRowMapper = new StudentAddressRowMapper();
    }

    @Override
    public Long addAddress(StudentAddress address) {
        return jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(
                            "{ call STUDENT_ADDRESS_PKG.ADD_ADDRESS(?, ?, ?, ?, ?, ?, ?, ?) }"
                    );

                    cs.setLong(1, address.getStudentId());
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
    public void updateAddress(StudentAddress address) {
        jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(
                            "{ call STUDENT_ADDRESS_PKG.UPDATE_ADDRESS(?, ?, ?, ?, ?, ?, ?) }"
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
    public void deleteAddress(Long addressId) {
        jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(
                            "{ call STUDENT_ADDRESS_PKG.DELETE_ADDRESS(?) }"
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
    public Optional<StudentAddress> getAddressById(Long addressId) {
        return jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(
                            "{ call STUDENT_ADDRESS_PKG.GET_ADDRESS_BY_ID(?, ?) }"
                    );

                    cs.setLong(1, addressId);
                    cs.registerOutParameter(2, OracleTypes.CURSOR);

                    return cs;
                },
                (CallableStatementCallback<Optional<StudentAddress>>) cs -> {
                    cs.execute();

                    try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                        if (rs.next()) {
                            return Optional.of(addressRowMapper.mapRow(rs, 0));
                        }

                        return Optional.empty();
                    }
                }
        );
    }

    @Override
    public List<StudentAddress> getAddressesByStudent(Long studentId) {
        return jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(
                            "{ call STUDENT_ADDRESS_PKG.GET_ADDRESSES_BY_STUDENT(?, ?) }"
                    );

                    cs.setLong(1, studentId);
                    cs.registerOutParameter(2, OracleTypes.CURSOR);

                    return cs;
                },
                (CallableStatementCallback<List<StudentAddress>>) cs -> {
                    cs.execute();

                    List<StudentAddress> addresses = new ArrayList<>();

                    try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                        int rowNum = 0;

                        while (rs.next()) {
                            addresses.add(addressRowMapper.mapRow(rs, rowNum++));
                        }
                    }

                    return addresses;
                }
        );
    }
}
