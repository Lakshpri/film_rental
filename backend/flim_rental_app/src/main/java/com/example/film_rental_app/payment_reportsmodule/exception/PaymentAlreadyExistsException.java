package com.example.film_rental_app.payment_reportsmodule.exception;

import com.example.film_rental_app.common.exception.DuplicateResourceException;

public class PaymentAlreadyExistsException extends DuplicateResourceException {
    public PaymentAlreadyExistsException(Integer rentalId) {
        super("A payment has already been made for Rental " + rentalId + ". Each rental can only be paid once.");
    }
    public PaymentAlreadyExistsException(String field, Object value) {
        super("Payment", field, value);
    }
}
