package com.example.film_rental_app.customer_inventory_rentalmodule.exception;

import com.example.film_rental_app.common.exception.InvalidOperationException;

public class InventoryUnavailableException extends InvalidOperationException {
    public InventoryUnavailableException(Integer inventoryId) {
        super("This item (ID: " + inventoryId + ") is currently rented out and not available. "
                + "It must be returned first before it can be rented again.");
    }
    public InventoryUnavailableException(String message) {
        super(message);
    }
}
