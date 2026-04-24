package com.example.film_rental_app.master_datamodule.exception;

import com.example.film_rental_app.common.exception.InvalidOperationException;

public class CountryInvalidOperationException extends InvalidOperationException {
    public CountryInvalidOperationException(Integer countryId, String reason) {
        super(reason);
    }
}
