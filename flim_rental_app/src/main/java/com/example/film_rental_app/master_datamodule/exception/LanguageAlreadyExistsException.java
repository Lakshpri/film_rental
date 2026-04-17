package com.example.film_rental_app.master_datamodule.exception;

import com.example.film_rental_app.common.exception.DuplicateResourceException;

public class LanguageAlreadyExistsException extends DuplicateResourceException {
    public LanguageAlreadyExistsException(String languageName) {
        super("Language", "name", languageName);
    }
    public LanguageAlreadyExistsException(String field, Object value) {
        super("Language", field, value);
    }
}
