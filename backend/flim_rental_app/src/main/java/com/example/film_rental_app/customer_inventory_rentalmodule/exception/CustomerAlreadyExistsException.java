package com.example.film_rental_app.customer_inventory_rentalmodule.exception;

import com.example.film_rental_app.common.exception.DuplicateResourceException;

/** Extends DuplicateResourceException (HTTP 409).
 *  Thrown when the user tries to create a Customer with an email that already exists. */
public class CustomerAlreadyExistsException extends DuplicateResourceException {
    public CustomerAlreadyExistsException(String email) {
        super("Customer", "email", email);
    }
}
