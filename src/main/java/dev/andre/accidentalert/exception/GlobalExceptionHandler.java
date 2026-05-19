package dev.andre.accidentalert.exception;


import dev.andre.accidentalert.ambulatory.dto.response.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDTO> handleResponseStatus(
            ResponseStatusException ex,
            HttpServletRequest request
    ) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                ex.getStatusCode().value(),
                ex.getStatusCode().toString(),
                ex.getReason(),
                request.getRequestURI()
        );

        return ResponseEntity.status(ex.getStatusCode()).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneric(
            Exception ex,
            HttpServletRequest request
    ) {
        log.error("Unexpected internal error", ex);

        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                500,
                "Internal Server Error",
                "An unexpected error occurred. Please try again later.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        log.error("Request bodi is missing or malformed", ex);

        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                400,
                "Bad Request",
                "Request body cannot be read",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public  ResponseEntity<ErrorResponseDTO> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request) {

        log.error("You do not have permission to acess this resource", ex);

        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                403,
                "Access Denied",
                "You are not allowed to access this resource",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        log.error("Database integrity violation", ex);

        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                409,
                "Conflict",
                "Database constraint violation",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        StringBuilder errorMessage = new StringBuilder("Validation failed for arguments: ");
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMessage.append(String.format("[%s: %s] ", error.getField(), error.getDefaultMessage()));
        });

        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                400,
                "Bad Request, validation failed for one or more arguments",
                errorMessage.toString(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentials(
            BadCredentialsException ex,
            HttpServletRequest request) {

        log.error("Invalid username or password", ex);
        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                401,
                "Unauthorized",
                "Invalid username or password",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

}
