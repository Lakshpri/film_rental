package com.example.film_rental_app.master_datamodule.exception;

import com.example.film_rental_app.common.exception.DuplicateResourceException;

public class CategoryAlreadyExistsException extends DuplicateResourceException {
    public CategoryAlreadyExistsException(String categoryName) {
        super("Category", "name", categoryName);
    }
    public CategoryAlreadyExistsException(String field, Object value) {
        super("Category", field, value);
    }
}
