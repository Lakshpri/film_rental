package com.example.film_rental_app.common.exception;

public class ResourceNotFoundException extends RuntimeException {

    private final String resourceName;
    private final Object resourceId;

    public ResourceNotFoundException(String resourceName, Object resourceId) {
        super(String.format(
                "[404 - RESOURCE NOT FOUND]\n" +
                        "  Resource : %s\n" +
                        "  ID Given : %s\n" +
                        "  Problem  : No %s record was found with the given ID.\n" +
                        "  Fix      : Make sure the ID is correct and the record actually exists. " +
                        "Use the GET-all endpoint to browse available IDs.",
                resourceName, resourceId, resourceName));
        this.resourceName = resourceName;
        this.resourceId = resourceId;
    }

    public ResourceNotFoundException(String message) {
        super(message);
        this.resourceName = null;
        this.resourceId = null;
    }

    public String getResourceName() { return resourceName; }
    public Object getResourceId()   { return resourceId; }
    }
