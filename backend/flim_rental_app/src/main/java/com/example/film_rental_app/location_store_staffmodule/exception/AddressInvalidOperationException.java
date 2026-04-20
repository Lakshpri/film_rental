package com.example.film_rental_app.location_store_staffmodule.exception;

import com.example.film_rental_app.common.exception.InvalidOperationException;

public class AddressInvalidOperationException extends InvalidOperationException {
    public AddressInvalidOperationException(Integer addressId, String reason) {
        super(reason);
    }
    public AddressInvalidOperationException(String message) {
        super(message);
    }
}
