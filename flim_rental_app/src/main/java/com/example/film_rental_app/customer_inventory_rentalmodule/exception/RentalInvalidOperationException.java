package com.example.film_rental_app.customer_inventory_rentalmodule.exception;

import com.example.film_rental_app.common.exception.InvalidOperationException;

/** Extends InvalidOperationException (HTTP 400).
 *  Thrown when a business rule is violated on a Rental
 *  (e.g. returning a Rental that was already returned, or renting for an inactive Customer). */
public class RentalInvalidOperationException extends InvalidOperationException {
    public RentalInvalidOperationException(Integer rentalId, String reason) {
        super("Cannot perform this operation on Rental with ID = " + rentalId + ". Reason: " + reason);
    }
    public RentalInvalidOperationException(String message) {
        super(message);
    }
}
