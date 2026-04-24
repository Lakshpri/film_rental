package com.example.film_rental_app.location_store_staffmodule.exception;

import com.example.film_rental_app.common.exception.DuplicateResourceException;

public class StoreAlreadyExistsException extends DuplicateResourceException {
    public StoreAlreadyExistsException(Integer addressId) {
        super("A store already exists at this address (Address ID: " + addressId + "). Each address can only have one store.");
    }
}
