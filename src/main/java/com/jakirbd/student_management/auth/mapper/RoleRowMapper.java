package com.jakirbd.student_management.auth.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.jakirbd.student_management.auth.model.Role;

public class RoleRowMapper implements RowMapper<Role> {

    @Override
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {

        Role role = new Role();

        role.setRoleId(rs.getLong("ROLE_ID"));
        role.setRoleName(rs.getString("ROLE_NAME"));
        role.setDescription(rs.getString("DESCRIPTION"));
        role.setStatus(rs.getString("STATUS"));

        Timestamp createdAt = rs.getTimestamp("CREATED_AT");
        if (createdAt != null) {
            role.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("UPDATED_AT");
        if (updatedAt != null) {
            role.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return role;
    }
}
