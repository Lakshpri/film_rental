package com.example.film_rental_app.customer_inventory_rentalmodule.exception;

import com.example.film_rental_app.common.exception.DuplicateResourceException;

/** Extends DuplicateResourceException (HTTP 409).
 *  Thrown when the user tries to create a Rental that already exists
 *  for the same Customer + Inventory item without a return. */
public class RentalAlreadyExistsException extends DuplicateResourceException {
    public RentalAlreadyExistsException(Integer customerId, Integer inventoryId) {
        super("Rental", "customerId + inventoryId", "customerId=" + customerId + ", inventoryId=" + inventoryId);
    }
    public RentalAlreadyExistsException(String field, Object value) {
        super("Rental", field, value);
    }
}
