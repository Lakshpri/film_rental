package com.example.film_rental_app.Location_Store_StaffModule.exception;

import com.example.film_rental_app.common.exception.ResourceNotFoundException;

public class AddressNotFoundException extends ResourceNotFoundException {

    public AddressNotFoundException(Integer addressId) {
        super("Address", addressId);
    }

    public AddressNotFoundException(String message) {
        super(message);
    }
}
