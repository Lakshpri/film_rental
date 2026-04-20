package com.example.film_rental_app.location_store_staffmodule.exception;

import com.example.film_rental_app.common.exception.InvalidOperationException;

public class StoreInvalidOperationException extends InvalidOperationException {
    public StoreInvalidOperationException(Integer storeId, String reason) {
        super(reason);
    }
    public StoreInvalidOperationException(String message) {
        super(message);
    }
}
