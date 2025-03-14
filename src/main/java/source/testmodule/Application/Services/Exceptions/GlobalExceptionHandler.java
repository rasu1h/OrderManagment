package source.testmodule.Application.Services.Exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.security.auth.message.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_LOG_TEMPLATE = "Handling {}: {}";

    @ExceptionHandler({AuthorizationDeniedException.class, PermissionDeniedDataAccessException.class})
    public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(
            AuthorizationDeniedException ex,
            WebRequest request
    ) {
        log.warn("Authorization denied: {}", ex.getMessage());
        return buildErrorResponse(
                HttpStatus.FORBIDDEN, //403
                "Access Denied",
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
            EntityNotFoundException ex,
            WebRequest request
    ) {
        log.warn(ERROR_LOG_TEMPLATE, ex.getClass().getSimpleName(), ex.getMessage());
        return buildErrorResponse(
                HttpStatus.NOT_FOUND, //404
                "Entity not found",
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler({
            AuthException.class,
            BadCredentialsException.class,
            ExpiredJwtException.class
    })
    public ResponseEntity<ErrorResponse> handleAuthenticationExceptions(
            RuntimeException ex, WebRequest request
    ) {
        log.warn(ERROR_LOG_TEMPLATE, ex.getClass().getSimpleName(), ex.getMessage());
        return buildErrorResponse(
                HttpStatus.UNAUTHORIZED, //401
                "Authentication failed",
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(
            IllegalStateException ex, WebRequest request
    ) {
        log.warn(ERROR_LOG_TEMPLATE, ex.getClass().getSimpleName(), ex.getMessage());
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST, //400
                "Invalid request state",
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, WebRequest request
    ) {
        log.warn(ERROR_LOG_TEMPLATE, ex.getClass().getSimpleName(), ex.getMessage());
        return buildErrorResponse(
                HttpStatus.CONFLICT, //409
                "Wrong data provided",
                ex.getMessage(),
                request
        );
    }
   @ExceptionHandler(InvalidDataAccessApiUsageException.class)
       public ResponseEntity<ErrorResponse> handleInvalidDataAccessApiUsageException(
               InvalidDataAccessApiUsageException ex, WebRequest request
       ) {
           log.warn(ERROR_LOG_TEMPLATE, ex.getClass().getSimpleName(), ex.getMessage());
           return buildErrorResponse(
                   HttpStatus.BAD_REQUEST,
                   "Invalid data access",
                   ex.getMessage(),
                   request
           );
       }
    // Additional exception handler for invalid arguments
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request
    ) {
        log.warn(ERROR_LOG_TEMPLATE, ex.getClass().getSimpleName(), ex.getMessage());
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid argument",
                ex.getMessage(),
                request
        );
    }

    // Additional exception handler for validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request
    ) {
        log.warn(ERROR_LOG_TEMPLATE, ex.getClass().getSimpleName(), ex.getMessage());
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Validation error",
                errors,
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllUncaughtExceptions(
            Exception ex, WebRequest request
    ) {
        log.error("Unhandled exception occurred: {}", ex.getMessage(), ex);
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, //500
                "Internal server error",
                "An unexpected error occurred",
                request
        );
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status,
                                                             String error,
                                                             String message,
                                                             WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                status.value(),
                error,
                message,
                request.getDescription(false)
        );
        return new ResponseEntity<>(response, status);
    }

    record ErrorResponse(
            Instant timestamp,
            int status,
            String error,
            String message,
            String path
    ) {}
}
