package com.example.film_rental_app.master_datamodule.exception;

import com.example.film_rental_app.common.exception.InvalidOperationException;

public class CategoryInvalidOperationException extends InvalidOperationException {
    public CategoryInvalidOperationException(Integer categoryId, String reason) {
        super(reason);
    }
}
