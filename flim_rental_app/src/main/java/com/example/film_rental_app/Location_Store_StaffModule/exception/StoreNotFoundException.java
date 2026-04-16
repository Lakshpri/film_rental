package com.example.film_rental_app.Location_Store_StaffModule.exception;

import com.example.film_rental_app.common.exception.ResourceNotFoundException;

public class StoreNotFoundException extends ResourceNotFoundException {

    public StoreNotFoundException(Integer storeId) {
        super("Store", storeId);
    }

    public StoreNotFoundException(String message) {
        super(message);
    }
}
