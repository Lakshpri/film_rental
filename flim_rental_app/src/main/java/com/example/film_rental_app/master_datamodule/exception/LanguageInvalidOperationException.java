package com.example.film_rental_app.master_datamodule.exception;

import com.example.film_rental_app.common.exception.InvalidOperationException;

public class LanguageInvalidOperationException extends InvalidOperationException {
    public LanguageInvalidOperationException(Integer languageId, String reason) {
        super(reason);
    }
    public LanguageInvalidOperationException(String message) {
        super(message);
    }
}
