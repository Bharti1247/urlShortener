package com.learn.linkShortener.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ShortUrlNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(
            ShortUrlNotFoundException ex,
            HttpServletRequest request) {

        log.warn("Short URL not found path={} message={}",
                 request.getRequestURI(),
                 ex.getMessage());

        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Short URL Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ShortUrlDisabledException.class)
    public ResponseEntity<ApiError> handleDisabled(
            ShortUrlDisabledException ex,
            HttpServletRequest request) {

        log.warn("Short URL disabled path={} message={}",
                 request.getRequestURI(),
                 ex.getMessage());

        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.GONE.value(),
                "Short URL Disabled",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.GONE).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unhandled exception occurred path={}",
                  request.getRequestURI(), ex);

        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Unexpected error occurred",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}
