package com.example.film_rental_app.customer_inventory_rentalmodule.exception;

import com.example.film_rental_app.common.exception.DuplicateResourceException;

public class RentalAlreadyExistsException extends DuplicateResourceException {
    public RentalAlreadyExistsException(Integer customerId, Integer inventoryId) {
        super("Customer " + customerId + " has already rented this item (ID: " + inventoryId + ") and has not returned it yet.");
    }
    public RentalAlreadyExistsException(String field, Object value) {
        super("Rental", field, value);
    }
}
