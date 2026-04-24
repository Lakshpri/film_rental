package com.example.film_rental_app.location_store_staffmodule.exception;

import com.example.film_rental_app.common.exception.ResourceNotFoundException;

public class StoreNotFoundException extends ResourceNotFoundException {

    public StoreNotFoundException(Integer storeId) {
        super("Store", storeId);
    }

}
