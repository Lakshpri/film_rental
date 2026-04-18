package com.example.film_rental_app.common.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* ── 400 : @Valid field validation failures ──────────────────────────── */
    // Triggered when user sends blank name, bad email, negative rate, etc.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> body = base(HttpStatus.BAD_REQUEST, "Validation Failed");
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fe.getField(), fe.getDefaultMessage());
        }
        body.put("errors",      fieldErrors);
        body.put("suggestion",  "Fix the highlighted fields and resubmit your request.");
        return response(body, HttpStatus.BAD_REQUEST);
    }

    /* ── 400 : Wrong type in path variable or request param ─────────────── */
    // Triggered when user sends /customers/abc instead of /customers/1
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, Object> body = base(HttpStatus.BAD_REQUEST, "Invalid Parameter Type");
        body.put("parameter",  ex.getName());
        body.put("givenValue", ex.getValue());
        body.put("message",    "'" + ex.getName() + "' must be a valid " +
                ex.getRequiredType().getSimpleName());
        body.put("suggestion", "Check the parameter type. Example: use a number for ID fields.");
        return response(body, HttpStatus.BAD_REQUEST);
    }

    /* ── 400 : Malformed JSON body ───────────────────────────────────────── */
    // Triggered when user sends broken/unparseable JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleUnreadable(HttpMessageNotReadableException ex) {
        Map<String, Object> body = base(HttpStatus.BAD_REQUEST, "Malformed Request Body");
        body.put("message",   "The request body could not be read. It may be missing or contain invalid JSON.");
        body.put("suggestion","Send a valid JSON body with the correct field names and types.");
        return response(body, HttpStatus.BAD_REQUEST);
    }

    /* ── 400 : Missing required request parameter ────────────────────────── */
    // Triggered when a required @RequestParam is missing from the URL
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParam(MissingServletRequestParameterException ex) {
        Map<String, Object> body = base(HttpStatus.BAD_REQUEST, "Missing Request Parameter");
        body.put("parameter", ex.getParameterName());
        body.put("message",   "Required parameter '" + ex.getParameterName() + "' is missing.");
        body.put("suggestion","Include all required query parameters in your request.");
        return response(body, HttpStatus.BAD_REQUEST);
    }

    /* ── 404 : EntityNotFoundException ──────────────────────────────────── */
    // Triggered when user gives an ID that doesn't exist in the DB
    // (JPA's built-in exception — no custom class needed)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFound(EntityNotFoundException ex) {
        Map<String, Object> body = base(HttpStatus.NOT_FOUND, "Record Not Found");
        body.put("message",   ex.getMessage() != null ? ex.getMessage()
                : "The requested record does not exist in the database.");
        body.put("suggestion","Verify the ID you provided. Use the GET-all endpoint to see existing records.");
        return response(body, HttpStatus.NOT_FOUND);
    }

    /* ── 404 : ResourceNotFoundException (your custom base class) ────────── */
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

    /* ── 409 : DuplicateResourceException ───────────────────────────────── */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicate(DuplicateResourceException ex) {
        Map<String, Object> body = base(HttpStatus.CONFLICT, "Duplicate Resource");
        body.put("exceptionType", ex.getClass().getSimpleName());
        body.put("resourceName",  ex.getResourceName());
        body.put("message",       ex.getMessage());
        body.put("suggestion",    "The record already exists. Use a different value or update the existing entry.");
        return response(body, HttpStatus.CONFLICT);
    }

    /* ── 409 : DataIntegrityViolationException ───────────────────────────── */
    // Triggered automatically when:
    // - Duplicate email already exists in DB
    // - Foreign key ID doesn't exist (storeId, addressId, languageId etc.)
    // - NOT NULL constraint violated at DB level
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrity(DataIntegrityViolationException ex) {
        Map<String, Object> body = base(HttpStatus.CONFLICT, "Data Integrity Violation");

        String message = ex.getMostSpecificCause().getMessage();

        if (message != null && message.toLowerCase().contains("duplicate")) {
            body.put("message",   "A record with the same value already exists in the database.");
            body.put("suggestion","Use a unique value for this field.");
        } else if (message != null && message.toLowerCase().contains("foreign key")) {
            body.put("message",   "The ID you provided does not exist in the database.");
            body.put("suggestion","Make sure the referenced ID (storeId, addressId, languageId, etc.) exists before using it.");
        } else if (message != null && message.toLowerCase().contains("null")) {
            body.put("message",   "A required field is missing at the database level.");
            body.put("suggestion","Ensure all required fields are provided in your request.");
        } else {
            body.put("message",   "A database constraint was violated.");
            body.put("suggestion","Check your request data for duplicate or invalid values.");
        }

        return response(body, HttpStatus.CONFLICT);
    }

    /* ── 400 : InvalidOperationException ────────────────────────────────── */
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