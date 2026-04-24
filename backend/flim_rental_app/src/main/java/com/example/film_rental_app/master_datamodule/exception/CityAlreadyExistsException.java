package com.example.film_rental_app.master_datamodule.exception;

import com.example.film_rental_app.common.exception.DuplicateResourceException;

public class CityAlreadyExistsException extends DuplicateResourceException {
    public CityAlreadyExistsException(String cityName, Integer countryId) {
        super("The city '" + cityName + "' already exists in this country (Country ID: " + countryId + "). Please use a different name.");
    }
}
