package com.example.film_rental_app.payment_reportsmodule.exception;

import com.example.film_rental_app.common.exception.InvalidOperationException;

/** Extends InvalidOperationException (HTTP 400).
 *  Thrown when a business rule is violated on a Payment
 *  (e.g. making a payment for a Rental that is already paid, or a zero/negative amount). */
public class PaymentInvalidOperationException extends InvalidOperationException {
    public PaymentInvalidOperationException(Integer paymentId, String reason) {
        super("Cannot perform this operation on Payment with ID = " + paymentId + ". Reason: " + reason);
    }
    public PaymentInvalidOperationException(String message) {
        super(message);
    }
}
