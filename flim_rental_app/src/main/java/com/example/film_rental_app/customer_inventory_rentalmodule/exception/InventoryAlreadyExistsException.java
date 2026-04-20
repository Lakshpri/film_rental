package com.example.film_rental_app.customer_inventory_rentalmodule.exception;

import com.example.film_rental_app.common.exception.DuplicateResourceException;

public class InventoryAlreadyExistsException extends DuplicateResourceException {
    public InventoryAlreadyExistsException(Integer filmId, Integer storeId) {
        super("This film (ID: " + filmId + ") already has an inventory entry at Store " + storeId + ". Duplicate entries are not allowed.");
    }
    public InventoryAlreadyExistsException(String field, Object value) {
        super("Inventory", field, value);
    }
}
