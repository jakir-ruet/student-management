package com.jakirbd.student_management.common.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /*
     * ------------------------------------------------------------
     * Authentication exceptions
     * ------------------------------------------------------------
     */

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>>
    handleInvalidCredentials(
            InvalidCredentialsException exception
    ) {
        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                exception.getMessage()
        );
    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<Map<String, Object>>
    handleAccountLocked(
            AccountLockedException exception
    ) {
        return buildResponse(
                HttpStatus.LOCKED,
                exception.getMessage()
        );
    }

    @ExceptionHandler(InactiveAccountException.class)
    public ResponseEntity<Map<String, Object>>
    handleInactiveAccount(
            InactiveAccountException exception
    ) {
        return buildResponse(
                HttpStatus.FORBIDDEN,
                exception.getMessage()
        );
    }

    /*
     * ------------------------------------------------------------
     * Resource and business exceptions
     * ------------------------------------------------------------
     */

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>>
    handleResourceNotFound(
            ResourceNotFoundException exception
    ) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                exception.getMessage()
        );
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, Object>>
    handleDuplicateResource(
            DuplicateResourceException exception
    ) {
        return buildResponse(
                HttpStatus.CONFLICT,
                exception.getMessage()
        );
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>>
    handleBusinessException(
            BusinessException exception
    ) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        );
    }

    /*
     * The project contains a custom IllegalArgumentException class.
     * Therefore both the custom exception and java.lang exception
     * are handled explicitly.
     */
    @ExceptionHandler({
            java.lang.IllegalArgumentException.class,
            com.jakirbd.student_management.common.exception
                    .IllegalArgumentException.class
    })
    public ResponseEntity<Map<String, Object>>
    handleIllegalArgument(
            RuntimeException exception
    ) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        );
    }

    /*
     * ------------------------------------------------------------
     * Request DTO validation
     * ------------------------------------------------------------
     */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>>
    handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception
    ) {
        Map<String, String> validationErrors =
                new LinkedHashMap<>();

        for (FieldError fieldError :
                exception.getBindingResult().getFieldErrors()) {

            validationErrors.put(
                    fieldError.getField(),
                    fieldError.getDefaultMessage()
            );
        }

        Map<String, Object> body = createResponseBody(
                HttpStatus.BAD_REQUEST,
                "Request validation failed"
        );

        body.put("validationErrors", validationErrors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>>
    handleConstraintViolation(
            ConstraintViolationException exception
    ) {
        Map<String, String> validationErrors =
                new LinkedHashMap<>();

        for (ConstraintViolation<?> violation :
                exception.getConstraintViolations()) {

            validationErrors.put(
                    violation.getPropertyPath().toString(),
                    violation.getMessage()
            );
        }

        Map<String, Object> body = createResponseBody(
                HttpStatus.BAD_REQUEST,
                "Request validation failed"
        );

        body.put("validationErrors", validationErrors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

    /*
     * ------------------------------------------------------------
     * Spring JDBC / Oracle exceptions
     * ------------------------------------------------------------
     */

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>>
    handleDataAccessException(
            DataAccessException exception
    ) {
        int oracleErrorCode =
                extractOracleErrorCode(exception);

        HttpStatus status =
                resolveHttpStatus(oracleErrorCode);

        String message =
                extractOracleMessage(exception);

        Map<String, Object> body =
                createResponseBody(status, message);

        if (oracleErrorCode != 0) {
            body.put("oracleErrorCode", oracleErrorCode);
        }

        if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
            LOGGER.error(
                    "Database operation failed. Oracle error code: {}",
                    oracleErrorCode,
                    exception
            );
        }

        return ResponseEntity.status(status).body(body);
    }

    /*
     * Handles database exceptions already translated by the
     * project's OracleExceptionTranslator.
     */
    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<Map<String, Object>>
    handleDatabaseException(
            DatabaseException exception
    ) {
        LOGGER.error(
                "Translated database operation failed",
                exception
        );

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "A database operation failed"
        );
    }

    /*
     * ------------------------------------------------------------
     * Unexpected exceptions
     * ------------------------------------------------------------
     */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>>
    handleUnexpectedException(
            Exception exception
    ) {
        LOGGER.error(
                "Unexpected application error",
                exception
        );

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected server error occurred"
        );
    }

    /*
     * ------------------------------------------------------------
     * Oracle helper methods
     * ------------------------------------------------------------
     */

    private int extractOracleErrorCode(
            Throwable throwable
    ) {
        Throwable current = throwable;

        while (current != null) {
            if (current instanceof SQLException sqlException) {
                SQLException currentSqlException =
                        sqlException;

                while (currentSqlException != null) {
                    int errorCode = Math.abs(
                            currentSqlException.getErrorCode()
                    );

                    if (errorCode != 0) {
                        return errorCode;
                    }

                    currentSqlException =
                            currentSqlException
                                    .getNextException();
                }
            }

            current = current.getCause();
        }

        return 0;
    }

    private String extractOracleMessage(
            Throwable throwable
    ) {
        Throwable current = throwable;

        while (current != null) {
            if (current instanceof SQLException sqlException) {
                SQLException currentSqlException =
                        sqlException;

                while (currentSqlException != null) {
                    String message =
                            currentSqlException.getMessage();

                    if (message != null
                            && !message.isBlank()) {
                        return cleanOracleMessage(message);
                    }

                    currentSqlException =
                            currentSqlException
                                    .getNextException();
                }
            }

            current = current.getCause();
        }

        return "A database error occurred";
    }

    private String cleanOracleMessage(String message) {
        if (message == null || message.isBlank()) {
            return "A database error occurred";
        }

        String cleanedMessage = message;

        int oraPosition =
                cleanedMessage.indexOf("ORA-");

        if (oraPosition >= 0) {
            cleanedMessage =
                    cleanedMessage.substring(oraPosition);
        }

        int linePosition =
                cleanedMessage.indexOf("\nORA-06512");

        if (linePosition >= 0) {
            cleanedMessage =
                    cleanedMessage.substring(
                            0,
                            linePosition
                    );
        }

        int helpPosition =
                cleanedMessage.indexOf("\nhttps://");

        if (helpPosition >= 0) {
            cleanedMessage =
                    cleanedMessage.substring(
                            0,
                            helpPosition
                    );
        }

        return cleanedMessage.trim();
    }

    private HttpStatus resolveHttpStatus(
            int oracleErrorCode
    ) {
        return switch (oracleErrorCode) {

            /*
             * Resources not found
             */
            case 20511,
                 20608,
                 20615,
                 20712,
                 20807,
                 20813,
                 20814
                    -> HttpStatus.NOT_FOUND;

            /*
             * Duplicate data and relationship conflicts
             */
            case 20508,
                 20513,
                 20609,
                 20617,
                 20706,
                 20709,
                 20716,
                 20810,
                 2291,
                 2292
                    -> HttpStatus.CONFLICT;

            /*
             * Unexpected PL/SQL and database failures
             */
            case 20509,
                 20512,
                 20514,
                 20613,
                 20616,
                 20618,
                 20710,
                 20713,
                 20717,
                 20811,
                 20815
                    -> HttpStatus.INTERNAL_SERVER_ERROR;

            /*
             * Business validation errors
             */
            case 20501,
                 20502,
                 20503,
                 20504,
                 20505,
                 20506,
                 20507,
                 20510,

                 20601,
                 20602,
                 20603,
                 20604,
                 20605,
                 20606,
                 20607,
                 20610,
                 20611,
                 20612,
                 20614,

                 20701,
                 20702,
                 20703,
                 20704,
                 20705,
                 20707,
                 20708,
                 20711,
                 20714,
                 20715,

                 20801,
                 20802,
                 20803,
                 20804,
                 20805,
                 20806,
                 20808,
                 20809,
                 20812
                    -> HttpStatus.BAD_REQUEST;

            default ->
                    HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    /*
     * ------------------------------------------------------------
     * Response helper methods
     * ------------------------------------------------------------
     */

    private ResponseEntity<Map<String, Object>>
    buildResponse(
            HttpStatus status,
            String message
    ) {
        return ResponseEntity
                .status(status)
                .body(createResponseBody(status, message));
    }

    private Map<String, Object> createResponseBody(
            HttpStatus status,
            String message
    ) {
        Map<String, Object> body =
                new LinkedHashMap<>();

        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put(
                "message",
                message == null || message.isBlank()
                        ? status.getReasonPhrase()
                        : message
        );

        return body;
    }
}