package com.jakirbd.student_management.auth.repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.jakirbd.student_management.auth.mapper.PermissionRowMapper;
import com.jakirbd.student_management.auth.mapper.RoleRowMapper;
import com.jakirbd.student_management.auth.mapper.UserRowMapper;
import com.jakirbd.student_management.auth.model.Permission;
import com.jakirbd.student_management.auth.model.Role;
import com.jakirbd.student_management.auth.model.User;
import com.jakirbd.student_management.common.exception.AccountLockedException;
import com.jakirbd.student_management.common.exception.DatabaseException;
import com.jakirbd.student_management.common.exception.DuplicateResourceException;
import com.jakirbd.student_management.common.exception.InactiveAccountException;
import com.jakirbd.student_management.common.exception.InvalidCredentialsException;

import oracle.jdbc.OracleTypes;

@Repository
public class AuthRepositoryImpl implements AuthRepository {

    private final JdbcTemplate jdbcTemplate;

    public AuthRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long register(User user) {
    try {
        return jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(
                            "{ call AUTH_PKG.REGISTER_USER(?, ?, ?, ?, ?) }"
                    );

                    cs.setString(1, user.getUsername());
                    cs.setString(2, user.getEmail());
                    cs.setString(3, user.getPasswordHash());
                    cs.setString(4, user.getFullName());
                    cs.registerOutParameter(5, Types.NUMERIC);

                    return cs;
                },
                (CallableStatementCallback<Long>) cs -> {
                    cs.execute();
                    return cs.getLong(5);
                }
        );

    } catch (DataAccessException ex) {
        SQLException sqlException = findSQLException(ex);

        if (sqlException != null && sqlException.getErrorCode() == 20001) {
            throw new DuplicateResourceException(
                    "Username or email already exists."
            );
        }

        throw ex;
    }
}

private SQLException findSQLException(Throwable ex) {
    Throwable current = ex;

    while (current != null) {
        if (current instanceof SQLException sqlException) {
            return sqlException;
        }

        current = current.getCause();
    }

    return null;
}

    @Override

    public Optional<User> login(String username, String passwordHash, String ipAddress) {

        try {

            Long userId = jdbcTemplate.execute(
                    (CallableStatementCreator) connection -> {

                        CallableStatement cs = connection.prepareCall(
                                "{ call AUTH_PKG.LOGIN_USER(?, ?, ?, ?, ?) }"
                        );

                        cs.setString(1, username);
                        cs.setString(2, passwordHash);
                        cs.setString(3, ipAddress);

                        cs.registerOutParameter(4, Types.NUMERIC);
                        cs.registerOutParameter(5, Types.VARCHAR);

                        return cs;
                    },

                    (CallableStatementCallback<Long>) cs -> {

                        cs.execute();

                        return cs.getLong(4);
                    }
            );

            return findById(userId);

        } catch (DataAccessException ex) {

            Throwable cause = ex.getCause();

            if (cause instanceof SQLException sqlEx) {

                switch (sqlEx.getErrorCode()) {

                    case 20003:
                        throw new AccountLockedException("User account is locked.");

                    case 20004:
                        throw new InactiveAccountException("User account is inactive.");

                    case 20005:
                        throw new InvalidCredentialsException("Invalid username or password.");

                    case 20006:
                        throw new InvalidCredentialsException("Invalid username or password.");

                    default:
                        throw new DatabaseException(sqlEx.getMessage());
                }
            }

            throw new DatabaseException("Database error occurred.", ex);
        }
    }

    @Override
    public void changePassword(Long userId, String oldPasswordHash, String newPasswordHash) {
        jdbcTemplate.update(
                "{ call AUTH_PKG.CHANGE_PASSWORD(?, ?, ?) }",
                userId,
                oldPasswordHash,
                newPasswordHash
        );
    }

    @Override
    public void lockUser(Long userId) {
        jdbcTemplate.update("{ call AUTH_PKG.LOCK_USER(?) }", userId);
    }

    @Override
    public void unlockUser(Long userId) {
        jdbcTemplate.update("{ call AUTH_PKG.UNLOCK_USER(?) }", userId);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = """
                SELECT USER_ID,
                       USERNAME,
                       EMAIL,
                       PASSWORD_HASH,
                       FULL_NAME,
                       STATUS,
                       FAILED_LOGIN,
                       LAST_LOGIN_AT,
                       CREATED_AT,
                       UPDATED_AT
                FROM USERS
                WHERE USERNAME = ?
                """;

        return jdbcTemplate.query(sql, new UserRowMapper(), username)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<User> findById(Long userId) {
        String sql = """
                SELECT USER_ID,
                       USERNAME,
                       EMAIL,
                       PASSWORD_HASH,
                       FULL_NAME,
                       STATUS,
                       FAILED_LOGIN,
                       LAST_LOGIN_AT,
                       CREATED_AT,
                       UPDATED_AT
                FROM USERS
                WHERE USER_ID = ?
                """;

        return jdbcTemplate.query(sql, new UserRowMapper(), userId)
                .stream()
                .findFirst();
    }

    @Override
    public List<Role> getUserRoles(Long userId) {
        return jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(
                            "{ call ROLE_PKG.GET_USER_ROLES(?, ?) }"
                    );

                    cs.setLong(1, userId);
                    cs.registerOutParameter(2, OracleTypes.CURSOR);

                    return cs;
                },
                (CallableStatementCallback<List<Role>>) cs -> {
                    cs.execute();

                    try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                        return new RoleRowMapper().mapRows(rs);
                    }
                }
        );
    }

    @Override
    public void assignRole(Long userId, Long roleId) {
        jdbcTemplate.update(
                "{ call ROLE_PKG.ASSIGN_ROLE_TO_USER(?, ?) }",
                userId,
                roleId
        );
    }

    @Override
    public void removeRole(Long userId, Long roleId) {
        jdbcTemplate.update(
                "{ call ROLE_PKG.REMOVE_ROLE_FROM_USER(?, ?) }",
                userId,
                roleId
        );
    }

    @Override
    public List<Permission> getUserPermissions(Long userId) {
        return jdbcTemplate.execute(
                (CallableStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(
                            "{ call PERMISSION_PKG.GET_USER_PERMISSIONS(?, ?) }"
                    );

                    cs.setLong(1, userId);
                    cs.registerOutParameter(2, OracleTypes.CURSOR);

                    return cs;
                },
                (CallableStatementCallback<List<Permission>>) cs -> {
                    cs.execute();

                    try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                        return new PermissionRowMapper().mapRows(rs);
                    }
                }
        );
    }
}
