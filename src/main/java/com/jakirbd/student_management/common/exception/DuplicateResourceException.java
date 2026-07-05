package com.jakirbd.student_management.common.exception;

public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
