package com.example.film_rental_app.location_store_staffmodule.exception;

import com.example.film_rental_app.common.exception.InvalidOperationException;

public class StaffInvalidOperationException extends InvalidOperationException {
    public StaffInvalidOperationException(Integer staffId, String reason) {
        super(reason);
    }
    public StaffInvalidOperationException(String message) {
        super(message);
    }
}
