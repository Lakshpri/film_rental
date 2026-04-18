package com.example.film_rental_app.location_store_staffmodule.exception;

import com.example.film_rental_app.common.exception.DuplicateResourceException;

public class AddressAlreadyExistsException extends DuplicateResourceException {
    public AddressAlreadyExistsException(String addressLine) {
        super("Address", "address", addressLine);
    }
    public AddressAlreadyExistsException(String field, Object value) {
        super("Address", field, value);
    }
}
