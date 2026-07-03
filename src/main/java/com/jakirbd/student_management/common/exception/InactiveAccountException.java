package com.jakirbd.student_management.common.exception;

public class InactiveAccountException extends RuntimeException {

    public InactiveAccountException(String message) {
        super(message);
    }
}