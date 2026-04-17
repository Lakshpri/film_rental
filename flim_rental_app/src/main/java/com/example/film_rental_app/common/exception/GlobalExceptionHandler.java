package com.example.film_rental_app.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* ── 404 : ResourceNotFoundException and all subclasses ─────────────── */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, Object> body = base(HttpStatus.NOT_FOUND, "Resource Not Found");
        body.put("exceptionType", ex.getClass().getSimpleName());
        body.put("resourceName",  ex.getResourceName());
        body.put("resourceId",    ex.getResourceId());
        body.put("message",       ex.getMessage());
        body.put("suggestion",    "Verify the ID you provided. Use the GET-all endpoint to see existing records.");
        return response(body, HttpStatus.NOT_FOUND);
    }

    /* ── 409 : DuplicateResourceException and all subclasses ────────────── */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicate(DuplicateResourceException ex) {
        Map<String, Object> body = base(HttpStatus.CONFLICT, "Duplicate Resource");
        body.put("exceptionType", ex.getClass().getSimpleName());
        body.put("resourceName",  ex.getResourceName());
        body.put("message",       ex.getMessage());
        body.put("suggestion",    "The record or link you tried to create already exists. Use a different value or update the existing entry.");
        return response(body, HttpStatus.CONFLICT);
    }

    /* ── 400 : InvalidOperationException and all subclasses ─────────────── */
    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidOperation(InvalidOperationException ex) {
        Map<String, Object> body = base(HttpStatus.BAD_REQUEST, "Invalid Operation");
        body.put("exceptionType", ex.getClass().getSimpleName());
        body.put("message",       ex.getMessage());
        body.put("suggestion",    "Check your request data and ensure all business rules are satisfied before retrying.");
        return response(body, HttpStatus.BAD_REQUEST);
    }

    /* ── 500 : fallback ──────────────────────────────────────────────────── */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        Map<String, Object> body = base(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        body.put("message",      "An unexpected error occurred. Please contact support if this persists.");
        body.put("debugMessage", ex.getMessage());
        return response(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* ── helpers ─────────────────────────────────────────────────────────── */
    private Map<String, Object> base(HttpStatus status, String error) {
        Map<String, Object> b = new LinkedHashMap<>();
        b.put("timestamp", LocalDateTime.now());
        b.put("status",    status.value());
        b.put("error",     error);
        return b;
    }

    private ResponseEntity<Map<String, Object>> response(Map<String, Object> body, HttpStatus status) {
        return new ResponseEntity<>(body, status);
    }
}
