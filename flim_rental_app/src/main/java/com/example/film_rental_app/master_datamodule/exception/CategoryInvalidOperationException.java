package com.example.film_rental_app.master_datamodule.exception;

import com.example.film_rental_app.common.exception.InvalidOperationException;

public class CategoryInvalidOperationException extends InvalidOperationException {
    public CategoryInvalidOperationException(Integer categoryId, String reason) {
        super("Cannot perform this operation on Category with ID = " + categoryId + ". Reason: " + reason);
    }
    public CategoryInvalidOperationException(String message) {
        super(message);
    }
}
