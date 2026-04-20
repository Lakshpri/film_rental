package com.example.film_rental_app.payment_reportsmodule.exception;

import com.example.film_rental_app.common.exception.InvalidOperationException;

/**
 * Extends InvalidOperationException (HTTP 400).
 * Thrown when a business rule is violated on a Payment.
 */
public class PaymentInvalidOperationException extends InvalidOperationException {

    // Case 1: Payment already exists
    public PaymentInvalidOperationException(Integer rentalId) {
        super("A payment has already been made for Rental " + rentalId + ". Each rental can only be paid once.");
    }

    // Case 2: Invalid amount
    public static PaymentInvalidOperationException invalidAmount() {
        return new PaymentInvalidOperationException("Payment amount must be greater than zero.");
    }

    // Case 3: Generic message
    public PaymentInvalidOperationException(Integer paymentId, String reason) {
        super("Cannot process Payment " + paymentId + ": " + reason);
    }

    // Base constructor
    public PaymentInvalidOperationException(String message) {
        super(message);
    }
}