package com.example.film_rental_app.Master_DataModule.exception;

import com.example.film_rental_app.common.exception.ResourceNotFoundException;

public class CountryNotFoundException extends ResourceNotFoundException {

    public CountryNotFoundException(Integer countryId) {
        super("Country", countryId);
    }

    public CountryNotFoundException(String message) {
        super(message);
    }
}
