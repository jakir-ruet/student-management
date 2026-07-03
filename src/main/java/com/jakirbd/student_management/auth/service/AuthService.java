package com.jakirbd.student_management.auth.service;

import com.jakirbd.student_management.auth.dto.AuthResponse;
import com.jakirbd.student_management.auth.dto.ChangePasswordRequest;
import com.jakirbd.student_management.auth.dto.LoginRequest;
import com.jakirbd.student_management.auth.dto.RegisterRequest;

public interface AuthService {
	Long register(RegisterRequest registerRequest);

	AuthResponse login(LoginRequest loginRequest, String ipAddress);

	void changePassword(ChangePasswordRequest changePasswordRequest);

	void lockUser(Long userId);

	void unlockUser(Long userId);
}
