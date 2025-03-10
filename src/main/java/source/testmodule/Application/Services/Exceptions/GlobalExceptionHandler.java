package source.testmodule.Application.Services.Exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.security.auth.message.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_LOG_TEMPLATE = "Handling {}: {}";
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(
            AuthorizationDeniedException ex,
            WebRequest request
    ) {
        log.warn("Authorization denied: {}", ex.getMessage());
        return buildErrorResponse(
                HttpStatus.FORBIDDEN, // Исправляем статус на 403
                "Access Denied",
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler({
            AuthException.class,
            BadCredentialsException.class,
            ExpiredJwtException.class
    })
    public ResponseEntity<ErrorResponse> handleAuthenticationExceptions(RuntimeException ex, WebRequest request) {
        log.warn(ERROR_LOG_TEMPLATE, ex.getClass().getSimpleName(), ex.getMessage());
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Authentication failed", ex.getMessage(), request);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
        log.warn(ERROR_LOG_TEMPLATE, ex.getClass().getSimpleName(), ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid request state", ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllUncaughtExceptions(Exception ex, WebRequest request) {
        log.error("Unhandled exception occurred: {}", ex.getMessage(), ex);
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
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