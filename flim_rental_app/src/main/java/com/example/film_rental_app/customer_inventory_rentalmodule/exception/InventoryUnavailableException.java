package com.example.film_rental_app.customer_inventory_rentalmodule.exception;

import com.example.film_rental_app.common.exception.InvalidOperationException;

public class InventoryUnavailableException extends InvalidOperationException {

    public InventoryUnavailableException(Integer inventoryId) {
        super("Inventory item with id " + inventoryId + " is currently not available for rental");
    }

    public InventoryUnavailableException(String message) {
        super(message);
    }
}
