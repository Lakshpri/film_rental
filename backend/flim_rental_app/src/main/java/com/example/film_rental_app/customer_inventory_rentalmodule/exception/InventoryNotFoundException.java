package com.example.film_rental_app.customer_inventory_rentalmodule.exception;

import com.example.film_rental_app.common.exception.ResourceNotFoundException;

/** Extends ResourceNotFoundException (HTTP 404).
 *  Thrown when an Inventory ID does not exist. */
public class InventoryNotFoundException extends ResourceNotFoundException {
    public InventoryNotFoundException(Integer inventoryId) {
        super("Inventory", inventoryId);
    }
    public InventoryNotFoundException(String message) {
        super(message);
    }
}
