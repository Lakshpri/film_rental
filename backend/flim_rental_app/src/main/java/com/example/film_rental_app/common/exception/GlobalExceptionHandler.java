package com.example.film_rental_app.common.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ── 1. Validation errors (@Valid on @RequestBody fields) ──────────────
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> body = base(HttpStatus.BAD_REQUEST, "Some fields have invalid values");
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fe.getField(), fe.getDefaultMessage());
        }
        body.put("fields",     fieldErrors);
        body.put("suggestion", "Please fix the fields shown above and try again.");
        return response(body, HttpStatus.BAD_REQUEST);
    }

    // ── 2. Negative or zero ID in URL path (@Positive on @PathVariable) ──
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, Object> body = base(HttpStatus.BAD_REQUEST, "Invalid ID");
        Map<String, String> fieldErrors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        v -> {
                            String path = v.getPropertyPath().toString();
                            return path.contains(".") ? path.substring(path.lastIndexOf('.') + 1) : path;
                        },
                        v -> v.getMessage(),
                        (a, b2) -> a
                ));
        body.put("fields",     fieldErrors);
        body.put("suggestion", "IDs must be positive numbers like 1, 2, 3. Negative numbers and zero are not valid.");
        return response(body, HttpStatus.BAD_REQUEST);
    }

    // ── 3. Text sent where a number is required (e.g. /customers/abc) ────
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, Object> body = base(HttpStatus.BAD_REQUEST, "Invalid value in the URL");
        body.put("field",      ex.getName());
        body.put("givenValue", String.valueOf(ex.getValue()));
        body.put("message",    "You entered '" + ex.getValue() + "' for '" + ex.getName() + "', but only numbers are accepted here.");
        body.put("suggestion", "Replace the value with a valid ID number. Example: /api/customers/1");
        return response(body, HttpStatus.BAD_REQUEST);
    }

    // ── 4. Empty or unreadable request body ───────────────────────────────
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleUnreadable(HttpMessageNotReadableException ex) {
        Map<String, Object> body = base(HttpStatus.BAD_REQUEST, "No data was received");
        body.put("message",    "We did not receive any data with your request, or the data you sent could not be read.");
        body.put("suggestion", "Please fill in all the required fields and try submitting again.");
        return response(body, HttpStatus.BAD_REQUEST);
    }

    // ── 5. Missing required @RequestParam ─────────────────────────────────
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParam(MissingServletRequestParameterException ex) {
        Map<String, Object> body = base(HttpStatus.BAD_REQUEST, "A required search value is missing");
        body.put("missingField", ex.getParameterName());
        body.put("message",    "The '" + ex.getParameterName() + "' value is required but was not provided.");
        body.put("suggestion", "Please include the required value and try again.");
        return response(body, HttpStatus.BAD_REQUEST);
    }

    // ── 6. Missing required @PathVariable ─────────────────────────────────
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<Map<String, Object>> handleMissingPathVar(MissingPathVariableException ex) {
        Map<String, Object> body = base(HttpStatus.BAD_REQUEST, "Incomplete URL");
        body.put("message",    "The URL is incomplete. It is missing the '" + ex.getVariableName() + "' value.");
        body.put("suggestion", "Make sure the URL includes all required parts. Example: /api/customers/1");
        return response(body, HttpStatus.BAD_REQUEST);
    }

    // ── 7. Business rule violated (delete active customer, etc.) ─────────
    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidOperation(InvalidOperationException ex) {
        Map<String, Object> body = base(HttpStatus.BAD_REQUEST, "This action is not allowed");
        body.put("reason",     ex.getMessage());
        body.put("suggestion", "Please follow the steps mentioned above before trying again.");
        return response(body, HttpStatus.BAD_REQUEST);
    }

    // ── 8. Record not found by ID ─────────────────────────────────────────
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, Object> body = base(HttpStatus.NOT_FOUND, "Record not found");
        if (ex.getResourceName() != null) {
            body.put("looking for", ex.getResourceName() + " with ID " + ex.getResourceId());
        }
        body.put("message",    ex.getMessage());
        body.put("suggestion", "Double-check the ID you entered. You can view all available records using the list screen.");
        return response(body, HttpStatus.NOT_FOUND);
    }

    // ── 9. JPA-level entity not found ─────────────────────────────────────
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFound(EntityNotFoundException ex) {
        Map<String, Object> body = base(HttpStatus.NOT_FOUND, "Record not found");
        body.put("message",    ex.getMessage() != null ? ex.getMessage() : "The record you are looking for does not exist.");
        body.put("suggestion", "Double-check the ID you entered. You can view all available records using the list screen.");
        return response(body, HttpStatus.NOT_FOUND);
    }

    // ── 10. URL does not exist (Spring Boot 3.x uses NoResourceFoundException) ──
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoResource(NoResourceFoundException ex) {
        Map<String, Object> body = base(HttpStatus.NOT_FOUND, "Page not found");
        body.put("message",    "The address you requested does not exist.");
        body.put("suggestion", "Check that the URL is correct and try again.");
        return response(body, HttpStatus.NOT_FOUND);
    }

    // ── 11. URL does not exist (fallback for older Spring) ────────────────
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoHandler(NoHandlerFoundException ex) {
        Map<String, Object> body = base(HttpStatus.NOT_FOUND, "Page not found");
        body.put("message",    "The address '" + ex.getRequestURL() + "' does not exist.");
        body.put("suggestion", "Check that the URL is correct and try again.");
        return response(body, HttpStatus.NOT_FOUND);
    }

    // ── 12. Duplicate record (same name, email, etc.) ─────────────────────
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicate(DuplicateResourceException ex) {
        Map<String, Object> body = base(HttpStatus.CONFLICT, "This record already exists");
        if (ex.getResourceName() != null) {
            body.put("type", ex.getResourceName());
        }
        body.put("message",    ex.getMessage());
        body.put("suggestion", "Try a different value, or find and update the existing record instead of creating a new one.");
        return response(body, HttpStatus.CONFLICT);
    }

    // ── 13. Database constraint violation (FK, duplicate, null) ──────────
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrity(DataIntegrityViolationException ex) {
        Map<String, Object> body = base(HttpStatus.CONFLICT, "Could not save the record");
        String msg = ex.getMostSpecificCause().getMessage();
        if (msg != null && msg.toLowerCase().contains("duplicate")) {
            body.put("message",    "A record with this value already exists. Please use a different value.");
            body.put("suggestion", "Change the duplicate value and try again.");
        } else if (msg != null && msg.toLowerCase().contains("foreign key")) {
            body.put("message",    "One of the IDs you selected does not exist in the system.");
            body.put("suggestion", "Make sure the Store, Address, Language, or other related record you selected actually exists.");
        } else if (msg != null && msg.toLowerCase().contains("null")) {
            body.put("message",    "A required field is empty. Please fill in all required fields.");
            body.put("suggestion", "Check that no required field is left blank and try again.");
        } else if (msg != null && msg.toLowerCase().contains("check")) {
            body.put("message",    "One of the values you entered is not within the allowed range.");
            body.put("suggestion", "Check all entered values (amounts, ratings, durations) and make sure they are valid.");
        } else {
            body.put("message",    "The record could not be saved because of a conflict with existing data.");
            body.put("suggestion", "Review your entries for duplicate or missing values and try again.");
        }
        return response(body, HttpStatus.CONFLICT);
    }

    // ── 14. Wrong content type sent ───────────────────────────────────────
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleMediaType(HttpMediaTypeNotSupportedException ex) {
        Map<String, Object> body = base(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Wrong format sent");
        body.put("message",    "The format you used to send data is not supported.");
        body.put("suggestion", "Please send your data in the correct format. Make sure you are using the right content type.");
        return response(body, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    // ── 15. Fallback for any unexpected error ─────────────────────────────
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        Map<String, Object> body = base(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
        body.put("message",    "An unexpected error occurred. Please try again in a moment.");
        body.put("suggestion", "If this keeps happening, please contact the system administrator.");
        return response(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ── Helpers ───────────────────────────────────────────────────────────
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