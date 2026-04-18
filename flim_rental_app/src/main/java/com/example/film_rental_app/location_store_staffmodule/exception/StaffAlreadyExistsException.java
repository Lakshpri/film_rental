package com.example.film_rental_app.location_store_staffmodule.exception;

import com.example.film_rental_app.common.exception.DuplicateResourceException;

public class StaffAlreadyExistsException extends DuplicateResourceException {
    public StaffAlreadyExistsException(String username) {
        super("Staff", "username", username);
    }
    public StaffAlreadyExistsException(String field, Object value) {
        super("Staff", field, value);
    }
}
