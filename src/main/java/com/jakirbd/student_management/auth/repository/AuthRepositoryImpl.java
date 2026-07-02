package com.jakirbd.student_management.auth.repository;

import java.sql.CallableStatement;

import org.springframework.jdbc.core.JdbcTemplate;

import com.sun.jdi.connect.spi.Connection;

public class AuthRepositoryImpl implements AuthRepository {

	private final JdbcTemplate jdbcTemplate;

	public AuthRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Long register(User user) {
		return jdbcTemplate.execute(Connection -> {
			CallableStatement cs = connection.prepareCall(
					"{call AUTH_PKG.REGISTER_USER}");
		});
	}
}
