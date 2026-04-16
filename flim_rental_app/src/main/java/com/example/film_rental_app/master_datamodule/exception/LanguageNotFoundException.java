package com.example.film_rental_app.master_datamodule.exception;

import com.example.film_rental_app.common.exception.ResourceNotFoundException;

public class LanguageNotFoundException extends ResourceNotFoundException {

    public LanguageNotFoundException(Integer languageId) {
        super("Language", languageId);
    }

    public LanguageNotFoundException(String message) {
        super(message);
    }
}