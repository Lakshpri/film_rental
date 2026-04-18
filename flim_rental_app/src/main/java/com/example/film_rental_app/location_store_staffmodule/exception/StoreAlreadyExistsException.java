package com.example.film_rental_app.location_store_staffmodule.exception;

import com.example.film_rental_app.common.exception.DuplicateResourceException;

public class StoreAlreadyExistsException extends DuplicateResourceException {
    public StoreAlreadyExistsException(Integer addressId) {
        super("Store", "addressId", addressId);
    }
    public StoreAlreadyExistsException(String field, Object value) {
        super("Store", field, value);
    }
}
