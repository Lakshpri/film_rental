package com.example.film_rental_app.common.exception;

public class DuplicateResourceException extends RuntimeException {

    private final String resourceName;

    public DuplicateResourceException(String resourceName, String field, Object value) {
        super(String.format(
                "[409 - DUPLICATE RESOURCE]\n" +
                        "  Resource : %s\n" +
                        "  Field    : %s\n" +
                        "  Value    : %s\n" +
                        "  Problem  : A %s record with %s = '%s' already exists in the system.\n" +
                        "  Fix      : Choose a different value for '%s', or update the existing record instead of creating a new one.",
                resourceName, field, value, resourceName, field, value, field));
        this.resourceName = resourceName;
    }

    public DuplicateResourceException(String message) {
        super(message);
        this.resourceName = null;
    }

    public String getResourceName() {
        return resourceName;
    }
}
