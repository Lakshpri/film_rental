package com.example.film_rental_app.location_store_staffmodule.exception;

import com.example.film_rental_app.common.exception.ResourceNotFoundException;

public class StaffNotFoundException extends ResourceNotFoundException {

    public StaffNotFoundException(Integer staffId) {
        super("Staff", staffId);
    }

    public StaffNotFoundException(String message) {
        super(message);
    }
}