package com.example.film_rental_app.customer_inventory_rentalmodule.exception;

import com.example.film_rental_app.common.exception.DuplicateResourceException;

/** Extends DuplicateResourceException (HTTP 409).
 *  Thrown when the user tries to add an Inventory entry that already exists for the same Film + Store. */
public class InventoryAlreadyExistsException extends DuplicateResourceException {
    public InventoryAlreadyExistsException(Integer filmId, Integer storeId) {
        super("Inventory", "filmId + storeId", "filmId=" + filmId + ", storeId=" + storeId);
    }
    public InventoryAlreadyExistsException(String field, Object value) {
        super("Inventory", field, value);
    }
}
