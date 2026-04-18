package com.example.film_rental_app.customer_inventory_rentalmodule.exception;

import com.example.film_rental_app.common.exception.InvalidOperationException;

/** Extends InvalidOperationException (HTTP 400).
 *  Thrown when the user tries to rent an Inventory item that is already rented out
 *  and has not been returned yet. */
public class InventoryUnavailableException extends InvalidOperationException {
    public InventoryUnavailableException(Integer inventoryId) {
        super("Inventory item with ID = " + inventoryId + " is currently rented out and not available. "
                + "It must be returned before it can be rented again.");
    }
    public InventoryUnavailableException(String message) {
        super(message);
    }
}
