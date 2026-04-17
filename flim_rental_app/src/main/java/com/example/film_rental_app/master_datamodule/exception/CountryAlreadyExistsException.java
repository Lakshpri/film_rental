package com.example.film_rental_app.master_datamodule.exception;

import com.example.film_rental_app.common.exception.DuplicateResourceException;

public class CountryAlreadyExistsException extends DuplicateResourceException {
    public CountryAlreadyExistsException(String countryName) {
        super("Country", "name", countryName);
    }
    public CountryAlreadyExistsException(String field, Object value) {
        super("Country", field, value);
    }
}
