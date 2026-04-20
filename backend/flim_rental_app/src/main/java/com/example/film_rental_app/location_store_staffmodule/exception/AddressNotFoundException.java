package com.example.film_rental_app.location_store_staffmodule.exception;

import com.example.film_rental_app.common.exception.ResourceNotFoundException;

public class AddressNotFoundException extends ResourceNotFoundException {

    public AddressNotFoundException(Integer addressId) {
        super("Address", addressId);
    }

    public AddressNotFoundException(String message) {
        super(message);
    }
}
