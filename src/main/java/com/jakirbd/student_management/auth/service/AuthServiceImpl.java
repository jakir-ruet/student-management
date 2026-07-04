package com.jakirbd.student_management.auth.service;

import com.jakirbd.student_management.auth.dto.AuthResponse;
import com.jakirbd.student_management.auth.dto.ChangePasswordRequest;
import com.jakirbd.student_management.auth.dto.LoginRequest;
import com.jakirbd.student_management.auth.dto.RegisterRequest;
import com.jakirbd.student_management.auth.model.Permission;
import com.jakirbd.student_management.auth.model.Role;
import com.jakirbd.student_management.auth.model.User;
import com.jakirbd.student_management.auth.repository.AuthRepository;
import com.jakirbd.student_management.common.exception.InvalidCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthServiceImpl implements AuthService {

	private final AuthRepository authRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthServiceImpl(AuthRepository authRepository,
	                       PasswordEncoder passwordEncoder) {
		this.authRepository = authRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Long register(RegisterRequest request) {
		User user = new User();

		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setFullName(request.getFullName());
		user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

		return authRepository.register(user);
	}

	@Override
	public AuthResponse login(LoginRequest request, String ipAddress) {
		User user = authRepository.findByUsername(request.getUsername())
				.orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

		if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
			throw new InvalidCredentialsException("Invalid username or password");
		}

		User loggedInUser = authRepository.login(
				request.getUsername(),
				user.getPasswordHash(),
				ipAddress
		).orElseThrow(() -> new InvalidCredentialsException("Login failed"));

		List<String> roles = authRepository.getUserRoles(loggedInUser.getUserId())
				.stream()
				.map(Role::getRoleName)
				.toList();

		List<String> permissions = authRepository.getUserPermissions(loggedInUser.getUserId())
				.stream()
				.map(Permission::getPermissionCode)
				.toList();

		AuthResponse response = new AuthResponse();
		response.setUserId(loggedInUser.getUserId());
		response.setUsername(loggedInUser.getUsername());
		response.setFullName(loggedInUser.getFullName());
		response.setStatus(loggedInUser.getStatus());
		response.setRoles(roles);
		response.setPermissions(permissions);

		return response;
	}

	@Override
	public void changePassword(ChangePasswordRequest request) {
		User user = authRepository.findById(request.getUserId())
				.orElseThrow(() -> new RuntimeException("User not found"));

		if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
			throw new RuntimeException("Old password is incorrect");
		}

		String newPasswordHash = passwordEncoder.encode(request.getNewPassword());

		authRepository.changePassword(
				request.getUserId(),
				user.getPasswordHash(),
				newPasswordHash
		);
	}

	@Override
	public void lockUser(Long userId) {
		authRepository.lockUser(userId);
	}

	@Override
	public void unlockUser(Long userId) {
		authRepository.unlockUser(userId);
	}
}