package com.example.film_rental_app.common.exception;

public class DuplicateResourceException extends RuntimeException {

    private final String resourceName;

    public DuplicateResourceException(String resourceName, String field, Object value) {
        super("A " + resourceName + " with " + field + " '" + value + "' already exists. Please use a different value.");
        this.resourceName = resourceName;
    }

    public DuplicateResourceException(String message) {
        super(message);
        this.resourceName = null;
    }

    public String getResourceName() { return resourceName; }
}
