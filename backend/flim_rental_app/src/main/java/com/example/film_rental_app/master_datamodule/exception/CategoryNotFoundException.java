package com.example.film_rental_app.master_datamodule.exception;

import com.example.film_rental_app.common.exception.ResourceNotFoundException;

public class CategoryNotFoundException extends ResourceNotFoundException {
    public CategoryNotFoundException(Integer categoryId) {
        super("Category", categoryId);
    }
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
