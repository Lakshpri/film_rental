package com.example.film_rental_app.payment_reportsmodule.exception;

import com.example.film_rental_app.common.exception.DuplicateResourceException;

/** Extends DuplicateResourceException (HTTP 409).
 *  Thrown when the user tries to create a duplicate Payment for the same Rental. */
public class PaymentAlreadyExistsException extends DuplicateResourceException {
    public PaymentAlreadyExistsException(Integer rentalId) {
        super("Payment", "rentalId", rentalId);
    }
    public PaymentAlreadyExistsException(String field, Object value) {
        super("Payment", field, value);
    }
}

