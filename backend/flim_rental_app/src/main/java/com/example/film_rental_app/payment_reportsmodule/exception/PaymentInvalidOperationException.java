package com.example.film_rental_app.payment_reportsmodule.exception;

import com.example.film_rental_app.common.exception.InvalidOperationException;

/**
 * Extends InvalidOperationException (HTTP 400).
 * Thrown when a business rule is violated on a Payment.
 */
public class PaymentInvalidOperationException extends InvalidOperationException {

    public PaymentInvalidOperationException(String message) {
        super(message);
    }
}