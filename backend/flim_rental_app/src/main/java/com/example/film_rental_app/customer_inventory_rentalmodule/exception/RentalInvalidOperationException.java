package com.example.film_rental_app.customer_inventory_rentalmodule.exception;

import com.example.film_rental_app.common.exception.InvalidOperationException;

public class RentalInvalidOperationException extends InvalidOperationException {
    public RentalInvalidOperationException(Integer rentalId, String reason) {
        super(reason);
    }
    public RentalInvalidOperationException(String message) {
        super(message);
    }
}
