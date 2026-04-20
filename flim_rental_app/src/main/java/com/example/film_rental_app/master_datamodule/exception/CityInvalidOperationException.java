package com.example.film_rental_app.master_datamodule.exception;

import com.example.film_rental_app.common.exception.InvalidOperationException;

public class CityInvalidOperationException extends InvalidOperationException {
    public CityInvalidOperationException(Integer cityId, String reason) {
        super(reason);
    }
    public CityInvalidOperationException(String message) {
        super(message);
    }
}
