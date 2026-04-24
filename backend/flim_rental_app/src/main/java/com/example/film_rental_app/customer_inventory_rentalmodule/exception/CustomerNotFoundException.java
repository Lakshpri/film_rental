package com.example.film_rental_app.customer_inventory_rentalmodule.exception;

import com.example.film_rental_app.common.exception.ResourceNotFoundException;

/** Extends ResourceNotFoundException (HTTP 404).
 *  Thrown when a Customer ID does not exist. */
public class CustomerNotFoundException extends ResourceNotFoundException {
    public CustomerNotFoundException(Integer customerId) {
        super("Customer", customerId);
    }
}
