package com.example.film_rental_app.customer_inventory_rentalmodule.exception;

import com.example.film_rental_app.common.exception.InvalidOperationException;

public class CustomerInvalidOperationException extends InvalidOperationException {
    public CustomerInvalidOperationException(Integer customerId, String reason) {
        super(reason);
    }
}
