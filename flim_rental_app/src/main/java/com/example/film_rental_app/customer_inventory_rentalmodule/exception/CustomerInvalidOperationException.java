package com.example.film_rental_app.customer_inventory_rentalmodule.exception;

import com.example.film_rental_app.common.exception.InvalidOperationException;

/** Extends InvalidOperationException (HTTP 400).
 *  Thrown when a business rule is violated on a Customer
 *  (e.g. deleting a Customer who still has active Rentals or unpaid Payments). */
public class CustomerInvalidOperationException extends InvalidOperationException {
    public CustomerInvalidOperationException(Integer customerId, String reason) {
        super("Cannot perform this operation on Customer with ID = " + customerId + ". Reason: " + reason);
    }
    public CustomerInvalidOperationException(String message) {
        super(message);
    }
}
