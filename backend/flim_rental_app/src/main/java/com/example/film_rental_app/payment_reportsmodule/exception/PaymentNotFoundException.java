package com.example.film_rental_app.payment_reportsmodule.exception;

import com.example.film_rental_app.common.exception.ResourceNotFoundException;

/**
 * Extends ResourceNotFoundException (HTTP 404).
 * Thrown when a Payment ID does not exist.
 */
public class PaymentNotFoundException extends ResourceNotFoundException {

    public PaymentNotFoundException(Integer paymentId) {
        super("Payment with ID " + paymentId + " was not found.");
    }


}