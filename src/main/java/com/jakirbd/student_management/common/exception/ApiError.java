package com.jakirbd.student_management.common.exception;

public class ApiError extends RuntimeException {
	public ApiError(String message){
        super(message);
    }
}
