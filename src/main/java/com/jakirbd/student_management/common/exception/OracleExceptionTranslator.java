package com.jakirbd.student_management.common.exception;

import org.springframework.dao.DataAccessException;

import java.sql.SQLException;

public final class OracleExceptionTranslator {

    private OracleExceptionTranslator() {
    }

    public static RuntimeException translate(
            DataAccessException exception
    ) {
        SQLException sqlException = findSQLException(exception);

        if (sqlException == null) {
            return new DatabaseException(
                    "Unexpected database error",
                    exception
            );
        }

        int errorCode = sqlException.getErrorCode();
        String message = cleanOracleMessage(
                sqlException.getMessage()
        );

        switch (errorCode) {

            case 20101:
                return new DuplicateResourceException(
                        "Subject code already exists"
                );

            case 20102:
                return new ResourceNotFoundException(
                        "Subject not found"
                );

            case 20201:
                return new DuplicateResourceException(
                        "Subject is already assigned to this academic class"
                );

            case 20202:
                return new ResourceNotFoundException(
                        "Class subject not found"
                );

            case 20301:
                return new DuplicateResourceException(
                        "Teacher subject assignment already exists"
                );

            case 20302:
                return new ResourceNotFoundException(
                        "Teacher subject assignment not found"
                );

            case 1:
                return new DuplicateResourceException(
                        "Duplicate record"
                );

            case 2291:
                return new BusinessException(
                        "Referenced parent record does not exist"
                );

            case 2292:
                return new BusinessException(
                        "Record cannot be deleted because dependent records exist"
                );

            default:
                return new DatabaseException(
                        message,
                        exception
                );
        }
    }

    private static SQLException findSQLException(
            Throwable throwable
    ) {
        Throwable current = throwable;

        while (current != null) {

            if (current instanceof SQLException) {
                return (SQLException) current;
            }

            current = current.getCause();
        }

        return null;
    }

    private static String cleanOracleMessage(String message) {

        if (message == null || message.isBlank()) {
            return "Database operation failed";
        }

        int oraIndex = message.indexOf("ORA-");

        if (oraIndex >= 0) {
            message = message.substring(oraIndex);
        }

        int lineBreak = message.indexOf('\n');

        if (lineBreak >= 0) {
            message = message.substring(0, lineBreak);
        }

        return message.trim();
    }
}