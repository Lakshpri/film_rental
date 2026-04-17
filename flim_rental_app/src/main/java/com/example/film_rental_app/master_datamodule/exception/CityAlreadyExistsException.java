package com.example.film_rental_app.master_datamodule.exception;

import com.example.film_rental_app.common.exception.DuplicateResourceException;

public class CityAlreadyExistsException extends DuplicateResourceException {
    public CityAlreadyExistsException(String cityName, Integer countryId) {
        super("City", "name", cityName + " (in country ID=" + countryId + ")");
    }
    public CityAlreadyExistsException(String field, Object value) {
        super("City", field, value);
    }
}
