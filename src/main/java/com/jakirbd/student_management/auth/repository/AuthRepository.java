package com.jakirbd.student_management.auth.repository;

import java.util.List;
import java.util.Optional;

import com.jakirbd.student_management.auth.model.Permission;
import com.jakirbd.student_management.auth.model.Role;
import com.jakirbd.student_management.auth.model.User;

public interface AuthRepository {

    // ==========================
    // User Authentication
    // ==========================

    Long register(User user);

    Optional<User> login(String username, String passwordHash, String ipAddress);

    void changePassword(Long userId, String oldPasswordHash, String newPasswordHash);

    void lockUser(Long userId);

    void unlockUser(Long userId);

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long userId);

    // ==========================
    // Roles
    // ==========================

    List<Role> getUserRoles(Long userId);

    void assignRole(Long userId, Long roleId);

    void removeRole(Long userId, Long roleId);

    // ==========================
    // Permissions
    // ==========================

    List<Permission> getUserPermissions(Long userId);

}
