package com.jakirbd.student_management.auth.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.jakirbd.student_management.auth.model.Permission;

public class PermissionRowMapper implements RowMapper<Permission> {

    @Override
    public Permission mapRow(ResultSet rs, int rowNum) throws SQLException {

        Permission permission = new Permission();

        permission.setPermissionId(rs.getLong("PERMISSION_ID"));
        permission.setPermissionCode(rs.getString("PERMISSION_CODE"));
        permission.setDescription(rs.getString("DESCRIPTION"));
        permission.setStatus(rs.getString("STATUS"));

        Timestamp createdAt = rs.getTimestamp("CREATED_AT");
        if (createdAt != null) {
            permission.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("UPDATED_AT");
        if (updatedAt != null) {
            permission.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return permission;
    }

    /**
     * Maps all rows from a ResultSet into a List<Permission>.
     */
    public List<Permission> mapRows(ResultSet rs) throws SQLException {

        List<Permission> permissions = new ArrayList<>();
        int rowNum = 0;

        while (rs.next()) {
            permissions.add(mapRow(rs, rowNum++));
        }

        return permissions;
    }
}
