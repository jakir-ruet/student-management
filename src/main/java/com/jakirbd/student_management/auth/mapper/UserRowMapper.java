package com.jakirbd.student_management.auth.mapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.jakirbd.student_management.auth.model.User;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();

        user.setUserId(rs.getLong("USER_ID"));
        user.setUsername(rs.getString("USERNAME"));
        user.setEmail(rs.getString("EMAIL"));
        user.setPasswordHash(rs.getString("PASSWORD_HASH"));
        user.setFullName(rs.getString("FULL_NAME"));
        user.setStatus(rs.getString("STATUS"));
        user.setFailedLogin(rs.getInt("FAILED_LOGIN"));

        Timestamp lastLoginAt = rs.getTimestamp("LAST_LOGIN_AT");
        if (lastLoginAt != null) {
            user.setLastLoginAt(lastLoginAt.toLocalDateTime());
        }

        Timestamp createdAt = rs.getTimestamp("CREATED_AT");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("UPDATED_AT");
        if (updatedAt != null) {
            user.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return user;
    }
}
