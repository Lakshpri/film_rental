package com.example.film_rental_app.Master_DataModule.exception;

import com.example.film_rental_app.common.exception.ResourceNotFoundException;

public class CityNotFoundException extends ResourceNotFoundException {

    public CityNotFoundException(Integer cityId) {
        super("City", cityId);
    }

    public CityNotFoundException(String message) {
        super(message);
    }
}

